package ma.uir.itgirlsbackend.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateMentoringReservationRequest(
        @NotBlank String studentName,
        @NotBlank String topic,
        @NotNull @Future LocalDateTime startsAt,
        @NotNull Long mentorUserId
) {}
