package ma.uir.itgirlsbackend.dto;

import jakarta.validation.constraints.NotBlank;

public record SendMessageContentRequest(
        @NotBlank String content
) {}
