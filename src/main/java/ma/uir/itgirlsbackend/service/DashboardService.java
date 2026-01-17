package ma.uir.itgirlsbackend.service;

import ma.uir.itgirlsbackend.dto.*;
import ma.uir.itgirlsbackend.repo.EventRepository;
import ma.uir.itgirlsbackend.repo.FormationRepository;
import ma.uir.itgirlsbackend.repo.MentoringReservationRepository;
import ma.uir.itgirlsbackend.repo.NotificationRepository;
import org.springframework.stereotype.Service;
import ma.uir.itgirlsbackend.domain.enums.MentoringStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DashboardService {

    private final FormationRepository formationRepository;
    private final EventRepository eventRepository;
    private final MentoringReservationRepository reservationRepository;
    private final NotificationRepository notificationRepository;

    public DashboardService(
            FormationRepository formationRepository,
            EventRepository eventRepository,
            MentoringReservationRepository reservationRepository,
            NotificationRepository notificationRepository
    ) {
        this.formationRepository = formationRepository;
        this.eventRepository = eventRepository;
        this.reservationRepository = reservationRepository;
        this.notificationRepository = notificationRepository;
    }

    public DashboardResponse getDashboard(Long expertId) {
        if (expertId == null) throw new RuntimeException("expertId est requis");

        LocalDateTime now = LocalDateTime.now();

        int activeTrainings = (int) formationRepository.countByExpertId(expertId);
        int upcomingEvents = (int) eventRepository.countByExpertIdAndStartsAtAfter(expertId, now);
        int pendingReservations = (int) reservationRepository.countByMentorUserIdAndStatus(expertId, MentoringStatus.EN_ATTENTE);
        int notificationsCount = (int) notificationRepository.countByExpertIdAndReadFlag(expertId, false);

        List<FormationDto> popular = formationRepository
                .findTop3ByExpertIdAndIsPopularTrueOrderByIdDesc(expertId)
                .stream().map(FormationDto::from).toList();

        List<FormationDto> trainingsPreview;
        if (popular.size() >= 3) {
            trainingsPreview = popular;
        } else {
            var latest = formationRepository.findTop3ByExpertIdOrderByIdDesc(expertId)
                    .stream().map(FormationDto::from).toList();

            var merged = new ArrayList<FormationDto>();
            merged.addAll(popular);
            for (var f : latest) {
                boolean exists = merged.stream().anyMatch(x -> x.id().equals(f.id()));
                if (!exists) merged.add(f);
                if (merged.size() == 3) break;
            }
            trainingsPreview = merged;
        }

        List<EventDto> eventsPreview = eventRepository
                .findTop3ByExpertIdOrderByStartsAtAsc(expertId)
                .stream().map(EventDto::from).toList();

        List<MentoringReservationDto> reservationsPreview = reservationRepository
                .findTop3ByMentorUserIdOrderByStartsAtAsc(expertId)
                .stream().map(MentoringReservationDto::from).toList();

        List<NotificationDto> notificationsPreview = notificationRepository
                .findTop5ByExpertIdOrderByCreatedAtDesc(expertId)
                .stream().map(NotificationDto::from).toList();

        return new DashboardResponse(
                activeTrainings,
                upcomingEvents,
                pendingReservations,
                notificationsCount,
                trainingsPreview,
                eventsPreview,
                reservationsPreview,
                notificationsPreview
        );
    }
}
