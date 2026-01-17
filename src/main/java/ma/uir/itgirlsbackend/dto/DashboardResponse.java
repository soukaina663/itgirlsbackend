package ma.uir.itgirlsbackend.dto;


import java.util.List;

public record DashboardResponse(
        int activeTrainings,
        int upcomingEvents,
        int pendingReservations,
        int notificationsCount,
        List<FormationDto> trainingsPreview,
        List<EventDto> eventsPreview,
        List<MentoringReservationDto> reservationsPreview,
        List<NotificationDto> notificationsPreview
) {}

