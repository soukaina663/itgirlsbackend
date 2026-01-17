package ma.uir.itgirlsbackend.domain;

import jakarta.persistence.*;
import lombok.*;
import ma.uir.itgirlsbackend.domain.enums.UserRole;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "users",
        uniqueConstraints = @UniqueConstraint(columnNames = "email")
)
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(nullable = false, unique = true, length = 180)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserRole role; // EXPERT | GIRL | ADMIN

    // ---- Profil Girl ----
    @Column(length = 40)
    private String level; // lyceenne/bac2/bac3/master/autre

    // ---- Profil Expert ----
    @Column(length = 200)
    private String profession;

    @Column(name = "is_mentor")
    private Boolean isMentor;

    @Column(name = "cv_file", length = 255)
    private String cvFile;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void onCreate() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (isMentor == null) isMentor = false;
    }
}
