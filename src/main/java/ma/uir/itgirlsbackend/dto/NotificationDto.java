package ma.uir.itgirlsbackend.dto;

import ma.uir.itgirlsbackend.domain.Notification;

import java.time.LocalDateTime;

public record NotificationDto(
        Long id,
        String type,          // ✅ String côté API
        String title,
        String text,
        String targetPath,
        LocalDateTime createdAt,
        Boolean readFlag
) {
    public static NotificationDto from(Notification n) {
        return new NotificationDto(
                n.getId(),
                n.getType().name(),   // ✅ Enum -> String
                n.getTitle(),
                n.getText(),
                n.getTargetPath(),
                n.getCreatedAt(),
                n.getReadFlag()
        );
    }
}
