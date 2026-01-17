package ma.uir.itgirlsbackend.dto;

public record CreateFeedbackRequest(
        String name,
        String text,
        Integer stars
) {}
