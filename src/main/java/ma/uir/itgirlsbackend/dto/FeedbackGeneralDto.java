package ma.uir.itgirlsbackend.dto;

import ma.uir.itgirlsbackend.domain.FeedbackGeneral;

import java.time.LocalDateTime;

public record FeedbackGeneralDto(
        Long id,
        String authorName,
        String authorTitle,
        String text,
        LocalDateTime createdAt
) {
    public static FeedbackGeneralDto from(FeedbackGeneral f) {
        return new FeedbackGeneralDto(
                f.getId(),
                f.getAuthorName(),
                f.getAuthorTitle(),
                f.getText(),
                f.getCreatedAt()
        );
    }
}
