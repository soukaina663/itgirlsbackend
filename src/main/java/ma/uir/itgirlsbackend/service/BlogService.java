package ma.uir.itgirlsbackend.service;

import ma.uir.itgirlsbackend.domain.BlogPost;
import ma.uir.itgirlsbackend.repo.BlogPostRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogService {

    private final BlogPostRepository blogPostRepository;

    public BlogService(BlogPostRepository blogPostRepository) {
        this.blogPostRepository = blogPostRepository;
    }

    public List<BlogPost> list(String category, String q) {
        if (category != null && !category.isBlank()) {
            return blogPostRepository.findByCategoryIgnoreCaseOrderByPublishDateDesc(category.trim());
        }
        if (q != null && !q.isBlank()) {
            return blogPostRepository.findByTitleContainingIgnoreCaseOrderByPublishDateDesc(q.trim());
        }
        return blogPostRepository.findAllByOrderByPublishDateDesc();
    }

    public BlogPost featuredOrNull() {
        return blogPostRepository.findFirstByFeaturedTrueOrderByPublishDateDesc().orElse(null);
    }
}

