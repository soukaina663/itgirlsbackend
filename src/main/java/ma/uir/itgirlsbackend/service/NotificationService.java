package ma.uir.itgirlsbackend.service;

import ma.uir.itgirlsbackend.domain.Notification;
import ma.uir.itgirlsbackend.repo.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository repo;

    public NotificationService(NotificationRepository repo) {
        this.repo = repo;
    }

    // ✅ Recommandé : expertId (Long)
    @Transactional(readOnly = true)
    public long unreadCount(Long expertId) {
        return repo.countByExpertIdAndReadFlag(expertId, false);
    }



    // ✅ Dernières notifications (Top 5) pour expertId
    @Transactional(readOnly = true)
    public List<Notification> latest(Long expertId) {
        return repo.findTop5ByExpertIdOrderByCreatedAtDesc(expertId);
    }

    // ✅ Marquer comme lue (sécurisé par expertId)
    @Transactional
    public void markAsRead(Long expertId, Long id) {
        Notification n = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found"));

        if (n.getExpertId() == null || !n.getExpertId().equals(expertId)) {
            throw new IllegalArgumentException("Not your notification");
        }

        n.setReadFlag(true);
        repo.save(n);
    }
}
