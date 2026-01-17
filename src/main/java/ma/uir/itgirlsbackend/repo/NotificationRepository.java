package ma.uir.itgirlsbackend.repo;

import ma.uir.itgirlsbackend.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByExpertId(Long expertId);
    List<Notification> findByExpertIdAndReadFlag(Long expertId, Boolean readFlag);

    long countByExpertIdAndReadFlag(Long expertId, Boolean readFlag);

    List<Notification> findTop5ByExpertIdOrderByCreatedAtDesc(Long expertId);
}
