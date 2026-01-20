package ma.uir.itgirlsbackend.controller;

import lombok.RequiredArgsConstructor;
import ma.uir.itgirlsbackend.domain.CommunityPost;
import ma.uir.itgirlsbackend.dto.CommunityPostDto;
import ma.uir.itgirlsbackend.security.AuthUser;
import ma.uir.itgirlsbackend.service.CommunityPostService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommunityPostManagementController {

    private final CommunityPostService service;

    @PostMapping("/community-posts")
    public CommunityPostDto create(@AuthenticationPrincipal AuthUser me, @RequestBody CommunityPost p) {
        p.setAuthorName(me.name());
        p.setAuthorUserId(me.id());
        return CommunityPostDto.from(service.create(p));
    }

    @DeleteMapping("/community-posts/{id}")
    public void delete(@AuthenticationPrincipal AuthUser me, @PathVariable Long id) {
        boolean isAdmin = "ADMIN".equalsIgnoreCase(me.role());
        service.delete(id, me.id(), isAdmin);
    }
}
