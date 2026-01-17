package ma.uir.itgirlsbackend.repo;

import ma.uir.itgirlsbackend.domain.FormationEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FormationEnrollmentRepository extends JpaRepository<FormationEnrollment, Long> {
    Optional<FormationEnrollment> findByFormationIdAndGirlUserId(Long formationId, Long girlUserId);
    long countByGirlUserId(Long girlUserId);
    List<FormationEnrollment> findByGirlUserIdOrderByEnrolledAtDesc(Long girlUserId);
}
