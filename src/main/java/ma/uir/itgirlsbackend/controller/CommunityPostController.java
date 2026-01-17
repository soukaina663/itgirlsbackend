package ma.uir.itgirlsbackend.controller;

import ma.uir.itgirlsbackend.dto.CommunityPostDto;
import ma.uir.itgirlsbackend.service.CommunityPostService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/community-posts")
public class CommunityPostController {

    private final CommunityPostService communityPostService;

    public CommunityPostController(CommunityPostService communityPostService) {
        this.communityPostService = communityPostService;
    }

    // GET /api/public/community-posts?limit=20
    @GetMapping
    public List<CommunityPostDto> list(@RequestParam(required = false) Integer limit) {
        return communityPostService.latest(limit).stream().map(CommunityPostDto::from).toList();
    }
}

