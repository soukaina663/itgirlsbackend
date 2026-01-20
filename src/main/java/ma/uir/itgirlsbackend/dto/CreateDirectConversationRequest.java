package ma.uir.itgirlsbackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateDirectConversationRequest(
        String title,

        @NotNull Long otherUserId,
        @NotBlank String otherUserName,

        // "EXPERT" ou "STUDENT" (ou "GIRL" si tu utilises ce nom)
        @NotBlank String otherUserRole
) {}
