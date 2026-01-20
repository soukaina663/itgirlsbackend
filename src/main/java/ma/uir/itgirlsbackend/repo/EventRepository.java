package ma.uir.itgirlsbackend.repo;

import ma.uir.itgirlsbackend.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByTypeKeyIgnoreCaseOrderByStartsAtAsc(String typeKey);
    List<Event> findAllByOrderByStartsAtAsc();
    List<Event> findByExpertIdOrderByStartsAtAsc(Long expertId);

    long countByExpertIdAndStartsAtAfter(Long expertId, java.time.LocalDateTime now);

    List<Event> findTop3ByExpertIdOrderByStartsAtAsc(Long expertId);
}
