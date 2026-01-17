package ma.uir.itgirlsbackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SendMessageRequest(
        @NotNull Long senderId,
        @NotBlank String senderName,
        @NotBlank String content
) {}
