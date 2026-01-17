package ma.uir.itgirlsbackend.repo;

import ma.uir.itgirlsbackend.domain.Mentor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MentorRepository extends JpaRepository<Mentor, Long> {

    List<Mentor> findAllByOrderByVerifiedDescNameAsc();

    List<Mentor> findByVerified(Boolean verified);
    Optional<Mentor> findByUserId(Long userId);
}
