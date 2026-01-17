package ma.uir.itgirlsbackend.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ConversationSummaryDto(
        Long id,
        String title,
        LocalDateTime updatedAt,
        int unreadCount,
        String lastMessagePreview,
        LocalDateTime lastMessageAt,
        List<ParticipantDto> participants
) {
    public record ParticipantDto(Long userId, String userName, String userRole) {}
}
