package ma.uir.itgirlsbackend.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "mentors",
        uniqueConstraints = @UniqueConstraint(columnNames = "user_id")
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Mentor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    private Boolean verified;
    private String name;
    private String title;

    @Column(length = 2000)
    private String bio;

    @Column(length = 1000)
    private String tagsCsv;
}
