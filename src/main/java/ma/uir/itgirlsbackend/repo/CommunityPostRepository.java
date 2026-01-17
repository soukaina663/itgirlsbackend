package ma.uir.itgirlsbackend.repo;

import ma.uir.itgirlsbackend.domain.CommunityPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommunityPostRepository extends JpaRepository<CommunityPost, Long> {

    List<CommunityPost> findAllByOrderByIdDesc();
}
