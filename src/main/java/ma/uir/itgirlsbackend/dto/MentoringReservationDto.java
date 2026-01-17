package ma.uir.itgirlsbackend.dto;

import ma.uir.itgirlsbackend.domain.MentoringReservation;
import java.time.LocalDateTime;

public record MentoringReservationDto(
        Long id,
        String studentName,
        String topic,
        LocalDateTime startsAt,
        String status
) {
    public static MentoringReservationDto from(MentoringReservation r) {
        return new MentoringReservationDto(
                r.getId(),
                r.getStudentName(),
                r.getTopic(),
                r.getStartsAt(),
                r.getStatus().name()
        );
    }
}
