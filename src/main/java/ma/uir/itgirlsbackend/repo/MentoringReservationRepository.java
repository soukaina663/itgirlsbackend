package ma.uir.itgirlsbackend.repo;

import ma.uir.itgirlsbackend.domain.MentoringReservation;
import ma.uir.itgirlsbackend.domain.enums.MentoringStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MentoringReservationRepository extends JpaRepository<MentoringReservation, Long> {

    List<MentoringReservation> findByMentorUserIdOrderByStartsAtAsc(Long mentorUserId);
    long countByMentorUserIdAndStatus(Long mentorUserId, MentoringStatus status);
    List<MentoringReservation> findTop3ByMentorUserIdOrderByStartsAtAsc(Long mentorUserId);

    List<MentoringReservation> findByStudentUserIdOrderByStartsAtAsc(Long studentUserId);
    Optional<MentoringReservation> findByIdAndMentorUserId(Long id, Long mentorUserId);
}
