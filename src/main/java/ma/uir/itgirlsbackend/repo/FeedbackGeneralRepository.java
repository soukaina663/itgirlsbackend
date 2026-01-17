package ma.uir.itgirlsbackend.repo;

import ma.uir.itgirlsbackend.domain.FeedbackGeneral;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackGeneralRepository extends JpaRepository<FeedbackGeneral, Long> {
    List<FeedbackGeneral> findAllByOrderByCreatedAtDesc();
}
