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
}

