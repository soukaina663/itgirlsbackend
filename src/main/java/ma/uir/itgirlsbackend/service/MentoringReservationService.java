package ma.uir.itgirlsbackend.service;

import ma.uir.itgirlsbackend.domain.MentoringReservation;
import ma.uir.itgirlsbackend.domain.Notification;
import ma.uir.itgirlsbackend.domain.enums.MentoringStatus;
import ma.uir.itgirlsbackend.domain.enums.NotificationType;
import ma.uir.itgirlsbackend.dto.CreateMentoringReservationRequest;
import ma.uir.itgirlsbackend.dto.MentoringReservationDto;
import ma.uir.itgirlsbackend.repo.MentoringReservationRepository;
import ma.uir.itgirlsbackend.repo.NotificationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MentoringReservationService {

    private final MentoringReservationRepository repo;
    private final NotificationRepository notificationRepository;

    public MentoringReservationService(
            MentoringReservationRepository repo,
            NotificationRepository notificationRepository
    ) {
        this.repo = repo;
        this.notificationRepository = notificationRepository;
    }

    public List<MentoringReservation> listAll(Long mentorUserId) {
        return repo.findByMentorUserIdOrderByStartsAtAsc(mentorUserId);
    }

    public MentoringReservationDto create(CreateMentoringReservationRequest req) {
        MentoringReservation saved = repo.save(
                MentoringReservation.builder()
                        .studentName(req.studentName())
                        .topic(req.topic())
                        .startsAt(req.startsAt())
                        .mentorUserId(req.mentorUserId())
                        .status(MentoringStatus.EN_ATTENTE)
                        .build()
        );

        notificationRepository.save(
                Notification.builder()
                        .expertId(req.mentorUserId())
                        .type(NotificationType.RESERVATION)
                        .title("Nouvelle réservation mentorat")
                        .text(req.studentName() + " a demandé une session : " + req.topic())
                        .targetPath("/expert/requests?tab=mentorat")
                        .createdAt(LocalDateTime.now())
                        .readFlag(false)
                        .build()
        );

        return MentoringReservationDto.from(saved);
    }
}
