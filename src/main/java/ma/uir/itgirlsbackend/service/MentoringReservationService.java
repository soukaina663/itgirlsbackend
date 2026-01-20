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
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional(readOnly = true)
    public List<MentoringReservation> listForMentor(Long mentorUserId) {
        return repo.findByMentorUserIdOrderByStartsAtAsc(mentorUserId);
    }

    @Transactional(readOnly = true)
    public List<MentoringReservation> listForGirl(Long girlUserId) {
        return repo.findByStudentUserIdOrderByStartsAtAsc(girlUserId);
    }

    /**
     * ✅ AJOUT : createForGirl avec le DTO UNIQUE (pas V2)
     * On ignore studentName du body et on prend girlName du token (sécurisé).
     */
    @Transactional
    public MentoringReservationDto createForGirl(Long girlId, String girlName, CreateMentoringReservationRequest req) {
        if (girlId == null) throw new IllegalArgumentException("girlId est obligatoire");
        if (req == null) throw new IllegalArgumentException("Requête vide");
        if (req.mentorUserId() == null) throw new IllegalArgumentException("mentorUserId est obligatoire");
        if (req.startsAt() == null) throw new IllegalArgumentException("startsAt est obligatoire");

        String topic = req.topic() == null ? "" : req.topic().trim();
        if (topic.isEmpty()) throw new IllegalArgumentException("topic est obligatoire");

        String safeGirlName = (girlName == null || girlName.trim().isEmpty()) ? "Girl" : girlName.trim();

        MentoringReservation saved = repo.save(
                MentoringReservation.builder()
                        .studentName(safeGirlName)
                        .studentUserId(girlId)
                        .topic(topic)
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
                        .text(safeGirlName + " a demandé une session : " + topic)
                        .targetPath("/expert/requests?tab=mentorat")
                        .createdAt(LocalDateTime.now())
                        .readFlag(false)
                        .build()
        );

        return MentoringReservationDto.from(saved);
    }

    @Transactional
    public MentoringReservationDto updateStatus(Long reservationId, Long mentorUserId, MentoringStatus status) {
        MentoringReservation r = repo.findByIdAndMentorUserId(reservationId, mentorUserId)
                .orElseThrow(() -> new RuntimeException("reservation introuvable ou accès refusé"));
        r.setStatus(status);
        return MentoringReservationDto.from(repo.save(r));
    }
}
