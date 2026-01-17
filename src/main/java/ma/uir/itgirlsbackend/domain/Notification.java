package ma.uir.itgirlsbackend.domain;

import jakarta.persistence.*;
import lombok.*;
import ma.uir.itgirlsbackend.domain.enums.NotificationType;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // destinataire (expert user id)
    @Column(nullable = false)
    private Long expertId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private NotificationType type;

    @Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String text;

    @Column(nullable = false)
    private String targetPath;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private Boolean readFlag;

    @PrePersist
    void onCreate() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (readFlag == null) readFlag = false;
    }
}
