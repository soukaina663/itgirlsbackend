package ma.uir.itgirlsbackend.repo;

import ma.uir.itgirlsbackend.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    long countByExpertIdAndStartsAtAfter(Long expertId, LocalDateTime now);

    List<Event> findTop3ByExpertIdOrderByStartsAtAsc(Long expertId);

    List<Event> findByTypeKeyIgnoreCaseOrderByStartsAtAsc(String typeKey);
    List<Event> findAllByOrderByStartsAtAsc();
}
