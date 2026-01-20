package ma.uir.itgirlsbackend.service;

import ma.uir.itgirlsbackend.domain.BlogPost;
import ma.uir.itgirlsbackend.repo.BlogPostRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    public BlogPost create(BlogPost p) {
        if (p.getTitle() == null || p.getTitle().isBlank()) throw new RuntimeException("title requis");
        if (p.getCategory() == null || p.getCategory().isBlank()) p.setCategory("Général");
        if (p.getPublishDate() == null) p.setPublishDate(LocalDate.now());
        if (p.getFeatured() == null) p.setFeatured(false);
        return blogPostRepository.save(p);
    }

    public BlogPost update(Long id, BlogPost patch, Long authorUserId, boolean isAdmin) {
        BlogPost existing = blogPostRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("post introuvable: " + id));

        if (!isAdmin && existing.getAuthorUserId() != null && !existing.getAuthorUserId().equals(authorUserId)) {
            throw new RuntimeException("Accès refusé (pas auteur)");
        }

        if (patch.getCategory() != null) existing.setCategory(patch.getCategory());
        if (patch.getTitle() != null) existing.setTitle(patch.getTitle());
        if (patch.getExcerpt() != null) existing.setExcerpt(patch.getExcerpt());
        if (patch.getPublishDate() != null) existing.setPublishDate(patch.getPublishDate());
        if (patch.getReadTimeMins() != null) existing.setReadTimeMins(patch.getReadTimeMins());
        if (patch.getFeatured() != null) existing.setFeatured(patch.getFeatured());

        return blogPostRepository.save(existing);
    }

    public void delete(Long id, Long authorUserId, boolean isAdmin) {
        BlogPost existing = blogPostRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("post introuvable: " + id));

        if (!isAdmin && existing.getAuthorUserId() != null && !existing.getAuthorUserId().equals(authorUserId)) {
            throw new RuntimeException("Accès refusé (pas auteur)");
        }

        blogPostRepository.deleteById(id);
    }
}
