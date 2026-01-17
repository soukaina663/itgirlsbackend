package ma.uir.itgirlsbackend.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // propriétaire (expert user id)
    @Column(nullable = false)
    private Long expertId;

    @Column(nullable = false)
    private String typeKey;

    @Column(nullable = false)
    private String typeLabel;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private LocalDateTime startsAt;

    private Integer durationMins;
    private Integer participantsCount;

    private String badge;
    private String cover;
}
