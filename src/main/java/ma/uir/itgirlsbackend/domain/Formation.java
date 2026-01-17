package ma.uir.itgirlsbackend.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "formations")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Formation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // propriétaire (expert user id)
    @Column(nullable = false)
    private Long expertId;

    private String themeKey;
    private String themeLabel;

    private String title;
    private String level;

    private Double rating;
    private Integer reviewsCount;
    private Integer enrolledCount;

    @Column(length = 1000)
    private String prerequisites;

    private Integer durationWeeks;
    private Integer hoursPerWeek;
    private Boolean selfPaced;
    private Integer lessonsCount;

    private String badge;
    private String thumbnail;

    private Boolean isPopular;
}
