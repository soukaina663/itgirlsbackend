package ma.uir.itgirlsbackend.dto;

import ma.uir.itgirlsbackend.domain.Message;

import java.time.LocalDateTime;

public record MessageDto(
        Long id,
        Long conversationId,
        Long senderId,
        String senderName,
        String content,
        LocalDateTime createdAt
) {
    public static MessageDto from(Message m) {
        return new MessageDto(
                m.getId(),
                m.getConversation().getId(),
                m.getSenderId(),
                m.getSenderName(),
                m.getContent(),
                m.getCreatedAt()
        );
    }
}
