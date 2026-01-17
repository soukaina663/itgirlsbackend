package ma.uir.itgirlsbackend.repo;

import ma.uir.itgirlsbackend.domain.EventRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EventRegistrationRepository extends JpaRepository<EventRegistration, Long> {
    Optional<EventRegistration> findByEventIdAndGirlUserId(Long eventId, Long girlUserId);
    long countByGirlUserId(Long girlUserId);
    List<EventRegistration> findByGirlUserIdOrderByRegisteredAtDesc(Long girlUserId);
}
