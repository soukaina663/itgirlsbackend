package ma.uir.itgirlsbackend.repo;

import ma.uir.itgirlsbackend.domain.BlogPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {

    List<BlogPost> findAllByOrderByPublishDateDesc();

    Optional<BlogPost> findFirstByFeaturedTrueOrderByPublishDateDesc();

    List<BlogPost> findByCategoryIgnoreCaseOrderByPublishDateDesc(String category);

    List<BlogPost> findByTitleContainingIgnoreCaseOrderByPublishDateDesc(String q);
}
