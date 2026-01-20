package ma.uir.itgirlsbackend.service;

import ma.uir.itgirlsbackend.domain.CommunityPost;
import ma.uir.itgirlsbackend.repo.CommunityPostRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommunityPostService {

    private final CommunityPostRepository repository;

    public CommunityPostService(CommunityPostRepository repository) {
        this.repository = repository;
    }

    public List<CommunityPost> latest(Integer limit) {
        var all = repository.findAllByOrderByIdDesc();
        if (limit == null || limit <= 0) return all;
        return all.stream().limit(limit).toList();
    }

    public CommunityPost create(CommunityPost p) {
        if (p.getTitle() == null || p.getTitle().isBlank()) throw new RuntimeException("title requis");
        if (p.getImageUrl() == null || p.getImageUrl().isBlank()) throw new RuntimeException("imageUrl requis");
        return repository.save(p);
    }

    public void delete(Long id, Long authorUserId, boolean isAdmin) {
        CommunityPost existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("post introuvable: " + id));
        if (!isAdmin && existing.getAuthorUserId() != null && !existing.getAuthorUserId().equals(authorUserId)) {
            throw new RuntimeException("Accès refusé (pas auteur)");
        }
        repository.deleteById(id);
    }
}
