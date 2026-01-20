package ma.uir.itgirlsbackend.domain;

import jakarta.persistence.*;
import lombok.*;
import ma.uir.itgirlsbackend.domain.enums.MentoringStatus;

import java.time.LocalDateTime;

@Entity
@Table(name="mentoring_reservations")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class MentoringReservation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String studentName;

    @Column(nullable = false)
    private String topic;

    @Column(nullable = false)
    private LocalDateTime startsAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MentoringStatus status;

    @Column(nullable = false)
    private Long mentorUserId;

    @Column(name = "student_user_id")
    private Long studentUserId;

}
