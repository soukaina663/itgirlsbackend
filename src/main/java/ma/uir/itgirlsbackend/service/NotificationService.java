package ma.uir.itgirlsbackend.service;

import ma.uir.itgirlsbackend.domain.Notification;
import ma.uir.itgirlsbackend.repo.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository repo;

    public NotificationService(NotificationRepository repo) {
        this.repo = repo;
    }

    public long unreadCount(String expertUid) {
        return repo.countByExpertUidAndReadFlag(expertUid, false);
    }

    public List<Notification> latest(String expertUid) {
        return repo.findTop5ByExpertUidOrderByCreatedAtDesc(expertUid);
    }

    public void markAsRead(String expertUid, Long id) {
        Notification n = repo.findById(id).orElseThrow();
        if (n.getExpertUid() != null && !n.getExpertUid().equals(expertUid)) {
            throw new IllegalArgumentException("Not your notification");
        }
        n.setReadFlag(true);
        repo.save(n);
    }
}

