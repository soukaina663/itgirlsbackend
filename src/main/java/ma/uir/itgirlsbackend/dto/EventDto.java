package ma.uir.itgirlsbackend.dto;

import ma.uir.itgirlsbackend.domain.Event;

import java.time.LocalDateTime;

public record EventDto(
        Long id,
        String typeKey,
        String typeLabel,
        String title,
        LocalDateTime startsAt,
        Integer durationMins,
        Integer participantsCount,
        String badge,
        String cover
) {
    public static EventDto from(Event e) {
        return new EventDto(
                e.getId(),
                e.getTypeKey(),
                e.getTypeLabel(),
                e.getTitle(),
                e.getStartsAt(),
                e.getDurationMins(),
                e.getParticipantsCount(),
                e.getBadge(),
                e.getCover()
        );
    }
}
