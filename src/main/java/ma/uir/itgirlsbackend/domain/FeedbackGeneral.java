package ma.uir.itgirlsbackend.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "feedbackgeneral")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class FeedbackGeneral {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String authorName;     // ex: "Julie D."

    @Column(nullable = false, length = 180)
    private String authorTitle;    // ex: "Développeuse junior"

    @Column(nullable = false, length = 2000)
    private String text;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void onCreate() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }
}
