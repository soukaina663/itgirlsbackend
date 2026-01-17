package ma.uir.itgirlsbackend.service;

import lombok.RequiredArgsConstructor;
import ma.uir.itgirlsbackend.domain.*;
import ma.uir.itgirlsbackend.domain.enums.NotificationType;
import ma.uir.itgirlsbackend.dto.*;
import ma.uir.itgirlsbackend.repo.*;
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

    @Transactional
    public Long createOrGetDirectConversation(CreateConversationRequest req) {
        var existing = conversationRepository.findDirectBetween(req.expertId(), req.studentId());
        if (existing.isPresent()) return existing.get().getId();

        LocalDateTime now = LocalDateTime.now();

        Conversation c = Conversation.builder()
                .title(req.title())
                .createdAt(now)
                .updatedAt(now)
                .build();
        c = conversationRepository.save(c);

        participantRepository.save(ConversationParticipant.builder()
                .conversation(c)
                .userId(req.expertId())
                .userName(req.expertName())
                .userRole("EXPERT")
                .joinedAt(now)
                .lastReadAt(now)
                .build());

        participantRepository.save(ConversationParticipant.builder()
                .conversation(c)
                .userId(req.studentId())
                .userName(req.studentName())
                .userRole("STUDENT")
                .joinedAt(now)
                .lastReadAt(now)
                .build());

        return c.getId();
    }

    @Transactional(readOnly = true)
    public List<ConversationSummaryDto> listInbox(Long userId) {
        List<Conversation> convs = conversationRepository.findForUser(userId);

        return convs.stream().map(c -> {
            var parts = participantRepository.findByConversationId(c.getId());

            var lastMsgOpt = messageRepository.findTop1ByConversationIdOrderByCreatedAtDesc(c.getId());
            String preview = lastMsgOpt.map(m -> {
                String s = m.getContent() == null ? "" : m.getContent().trim();
                return s.length() > 80 ? s.substring(0, 80) + "…" : s;
            }).orElse("");

            var lastAt = lastMsgOpt.map(Message::getCreatedAt).orElse(null);

            var me = participantRepository.findByConversationIdAndUserId(c.getId(), userId).orElse(null);

            LocalDateTime since = (me == null || me.getLastReadAt() == null)
                    ? LocalDateTime.of(1970, 1, 1, 0, 0)
                    : me.getLastReadAt();

            int unread = (int) messageRepository.countUnread(c.getId(), userId, since);

            List<ConversationSummaryDto.ParticipantDto> pDtos = parts.stream()
                    .map(p -> new ConversationSummaryDto.ParticipantDto(p.getUserId(), p.getUserName(), p.getUserRole()))
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

    @Transactional(readOnly = true)
    public List<MessageDto> getMessages(Long conversationId) {
        return messageRepository.findByConversationIdOrderByCreatedAtAsc(conversationId)
                .stream().map(MessageDto::from).toList();
    }

    @Transactional
    public MessageDto sendMessage(Long conversationId, SendMessageRequest req) {
        Conversation c = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation introuvable: " + conversationId));

        LocalDateTime now = LocalDateTime.now();

        Message m = Message.builder()
                .conversation(c)
                .senderId(req.senderId())
                .senderName(req.senderName())
                .content(req.content())
                .createdAt(now)
                .build();
        m = messageRepository.save(m);

        c.setUpdatedAt(now);
        conversationRepository.save(c);

        participantRepository.findByConversationIdAndUserId(conversationId, req.senderId())
                .ifPresent(p -> {
                    p.setLastReadAt(now);
                    participantRepository.save(p);
                });

        List<ConversationParticipant> participants = participantRepository.findByConversationId(conversationId);

        for (ConversationParticipant p : participants) {
            boolean isExpert = "EXPERT".equalsIgnoreCase(p.getUserRole());
            boolean isReceiver = !p.getUserId().equals(req.senderId());

            if (isExpert && isReceiver) {
                Notification n = Notification.builder()
                        .expertId(p.getUserId())
                        .type(NotificationType.MESSAGE)
                        .title("Nouveau message")
                        .text(req.senderName() + " : " + shortText(req.content()))
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
        LocalDateTime now = LocalDateTime.now();
        ConversationParticipant p = participantRepository
                .findByConversationIdAndUserId(conversationId, userId)
                .orElseThrow(() -> new RuntimeException("Participant introuvable pour user " + userId));
        p.setLastReadAt(now);
        participantRepository.save(p);
    }

    private static String shortText(String s) {
        if (s == null) return "";
        String t = s.trim();
        return t.length() > 90 ? t.substring(0, 90) + "…" : t;
    }
}
