package ma.uir.itgirlsbackend.service;

import lombok.RequiredArgsConstructor;
import ma.uir.itgirlsbackend.domain.Conversation;
import ma.uir.itgirlsbackend.domain.ConversationParticipant;
import ma.uir.itgirlsbackend.domain.Message;
import ma.uir.itgirlsbackend.domain.Notification;
import ma.uir.itgirlsbackend.domain.enums.NotificationType;
import ma.uir.itgirlsbackend.dto.ConversationSummaryDto;
import ma.uir.itgirlsbackend.dto.MessageDto;
import ma.uir.itgirlsbackend.repo.ConversationParticipantRepository;
import ma.uir.itgirlsbackend.repo.ConversationRepository;
import ma.uir.itgirlsbackend.repo.MessageRepository;
import ma.uir.itgirlsbackend.repo.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final ConversationRepository conversationRepository;
    private final ConversationParticipantRepository participantRepository;
    private final MessageRepository messageRepository;
    private final NotificationRepository notificationRepository;

    /**
     * Conversation directe ENTRE N'IMPORTE QUELS 2 USERS.
     * (girl<->girl, expert<->expert, expert<->girl)
     *
     * IMPORTANT:
     * - utilise ton conversationRepository.findDirectBetween(a,b)
     *   (il doit juste vérifier les 2 userIds, pas les rôles)
     */
    @Transactional
    public Long createOrGetDirectConversation(
            Long myId, String myName, String myRole,
            Long otherId, String otherName, String otherRole,
            String title
    ) {
        if (myId == null || otherId == null) throw new IllegalArgumentException("userId(s) obligatoire(s)");
        if (myId.equals(otherId)) throw new IllegalArgumentException("Conversation avec soi-même interdite");

        var existing = conversationRepository.findDirectBetween(myId, otherId);
        if (existing.isPresent()) return existing.get().getId();

        LocalDateTime now = LocalDateTime.now();

        String t = safeTrim(title);
        if (t == null || t.isBlank()) t = "Conversation";

        Conversation c = Conversation.builder()
                .title(t)
                .createdAt(now)
                .updatedAt(now)
                .build();
        c = conversationRepository.save(c);

        participantRepository.save(ConversationParticipant.builder()
                .conversation(c)
                .userId(myId)
                .userName(nonBlankOrFallback(myName, "User"))
                .userRole(nonBlankOrFallback(normalizeRole(myRole), "STUDENT"))
                .joinedAt(now)
                .lastReadAt(now)
                .build());

        participantRepository.save(ConversationParticipant.builder()
                .conversation(c)
                .userId(otherId)
                .userName(nonBlankOrFallback(otherName, "User"))
                .userRole(nonBlankOrFallback(normalizeRole(otherRole), "STUDENT"))
                .joinedAt(now)
                .lastReadAt(now)
                .build());

        return c.getId();
    }

    @Transactional(readOnly = true)
    public List<ConversationSummaryDto> listInbox(Long userId) {
        if (userId == null) throw new IllegalArgumentException("userId est obligatoire");

        List<Conversation> convs = conversationRepository.findForUser(userId);

        return convs.stream().map(c -> {
            var parts = participantRepository.findByConversationId(c.getId());

            var lastMsgOpt = messageRepository.findTop1ByConversationIdOrderByCreatedAtDesc(c.getId());
            String preview = lastMsgOpt.map(m -> shortText(m.getContent(), 80)).orElse("");
            var lastAt = lastMsgOpt.map(Message::getCreatedAt).orElse(null);

            var meOpt = participantRepository.findByConversationIdAndUserId(c.getId(), userId);
            LocalDateTime since = meOpt.map(ConversationParticipant::getLastReadAt)
                    .orElse(LocalDateTime.of(1970, 1, 1, 0, 0));

            int unread = (int) messageRepository.countUnread(c.getId(), userId, since);

            List<ConversationSummaryDto.ParticipantDto> pDtos = parts.stream()
                    .map(p -> new ConversationSummaryDto.ParticipantDto(
                            p.getUserId(),
                            p.getUserName(),
                            p.getUserRole()
                    ))
                    .toList();

            return new ConversationSummaryDto(
                    c.getId(),
                    c.getTitle(),
                    c.getUpdatedAt(),
                    unread,
                    preview,
                    lastAt,
                    pDtos
            );
        }).toList();
    }

    /**
     * Lecture messages: vérifie que userId est participant.
     */
    @Transactional(readOnly = true)
    public List<MessageDto> getMessagesForUser(Long conversationId, Long userId) {
        if (conversationId == null) throw new IllegalArgumentException("conversationId est obligatoire");
        if (userId == null) throw new IllegalArgumentException("userId est obligatoire");

        participantRepository.findByConversationIdAndUserId(conversationId, userId)
                .orElseThrow(() -> new RuntimeException("Accès refusé: vous n'êtes pas participant"));

        return messageRepository.findByConversationIdOrderByCreatedAtAsc(conversationId)
                .stream()
                .map(MessageDto::from)
                .toList();
    }

    /**
     * Envoi message:
     * - vérifie que sender est participant
     * - crée Message
     * - update updatedAt conversation
     * - lastReadAt sender = now
     * - notification -> destinataires EXPERT uniquement (car Notification.expertId)
     */
    @Transactional
    public MessageDto sendMessage(Long conversationId, Long senderId, String senderName, String contentRaw) {
        if (conversationId == null) throw new IllegalArgumentException("conversationId est obligatoire");
        if (senderId == null) throw new IllegalArgumentException("senderId est obligatoire");

        String content = safeTrim(contentRaw);
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("Le contenu du message ne peut pas être vide");
        }

        Conversation c = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation introuvable: " + conversationId));

        ConversationParticipant senderPart = participantRepository
                .findByConversationIdAndUserId(conversationId, senderId)
                .orElseThrow(() -> new RuntimeException("Accès refusé: l'expéditeur n'est pas participant"));

        LocalDateTime now = LocalDateTime.now();

        Message m = Message.builder()
                .conversation(c)
                .senderId(senderId)
                .senderName(nonBlankOrFallback(senderName, senderPart.getUserName()))
                .content(content)
                .createdAt(now)
                .build();
        m = messageRepository.save(m);

        c.setUpdatedAt(now);
        conversationRepository.save(c);

        senderPart.setLastReadAt(now);
        participantRepository.save(senderPart);

        // Notifier uniquement les EXPERT (Notification.expertId)
        List<ConversationParticipant> participants = participantRepository.findByConversationId(conversationId);
        for (ConversationParticipant p : participants) {
            boolean isReceiver = !p.getUserId().equals(senderId);
            boolean isExpert = "EXPERT".equalsIgnoreCase(p.getUserRole());

            if (isReceiver && isExpert) {
                Notification n = Notification.builder()
                        .expertId(p.getUserId())
                        .type(NotificationType.MESSAGE)
                        .title("Nouveau message")
                        .text(m.getSenderName() + " : " + shortText(content, 90))
                        .targetPath("/expert/messages?conversationId=" + conversationId)
                        .createdAt(now)
                        .readFlag(false)
                        .build();
                notificationRepository.save(n);
            }
        }

        return MessageDto.from(m);
    }

    @Transactional
    public void markConversationRead(Long conversationId, Long userId) {
        if (conversationId == null) throw new IllegalArgumentException("conversationId est obligatoire");
        if (userId == null) throw new IllegalArgumentException("userId est obligatoire");

        LocalDateTime now = LocalDateTime.now();

        ConversationParticipant p = participantRepository
                .findByConversationIdAndUserId(conversationId, userId)
                .orElseThrow(() -> new RuntimeException("Participant introuvable pour user " + userId));

        p.setLastReadAt(now);
        participantRepository.save(p);
    }

    // -------------------- Helpers --------------------

    private static String safeTrim(String s) {
        return s == null ? null : s.trim();
    }

    private static String nonBlankOrFallback(String v, String fallback) {
        String t = safeTrim(v);
        return (t == null || t.isBlank()) ? fallback : t;
    }

    private static String shortText(String s, int max) {
        if (s == null) return "";
        String t = s.trim();
        if (t.length() <= max) return t;
        return t.substring(0, max) + "…";
    }

    private static String normalizeRole(String role) {
        if (role == null) return "GIRL";
        String r = role.trim().toUpperCase();
        if (r.startsWith("ROLE_")) r = r.substring(5);
        // ✅ on garde GIRL tel quel
        if (r.equals("STUDENT")) return "GIRL";
        return r;
    }

}
