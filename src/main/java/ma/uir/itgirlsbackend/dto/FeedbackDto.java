package ma.uir.itgirlsbackend.dto;

import ma.uir.itgirlsbackend.domain.Feedback;

import java.time.LocalDateTime;

public record FeedbackDto(
        Long id,
        String name,
        String text,
        Integer stars,
        LocalDateTime createdAt
) {
    public static FeedbackDto from(Feedback f) {
        return new FeedbackDto(
                f.getId(),
                f.getName(),
                f.getText(),
                f.getStars(),
                f.getCreatedAt()
        );
    }
}
