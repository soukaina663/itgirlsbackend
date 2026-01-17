package ma.uir.itgirlsbackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateConversationRequest(
        String title,

        @NotNull Long expertId,
        @NotBlank String expertName,

        @NotNull Long studentId,
        @NotBlank String studentName
) {}
