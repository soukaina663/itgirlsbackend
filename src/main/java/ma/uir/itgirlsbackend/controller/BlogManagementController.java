package ma.uir.itgirlsbackend.controller;

import lombok.RequiredArgsConstructor;
import ma.uir.itgirlsbackend.domain.BlogPost;
import ma.uir.itgirlsbackend.dto.BlogPostDto;
import ma.uir.itgirlsbackend.security.AuthUser;
import ma.uir.itgirlsbackend.service.BlogService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BlogManagementController {

    private final BlogService blogService;

    @PostMapping("/blog/posts")
    public BlogPostDto create(@AuthenticationPrincipal AuthUser me, @RequestBody BlogPost p) {
        p.setAuthorName(me.name());
        p.setAuthorUserId(me.id());
        return BlogPostDto.from(blogService.create(p));
    }

    @PutMapping("/blog/posts/{id}")
    public BlogPostDto update(@AuthenticationPrincipal AuthUser me, @PathVariable Long id, @RequestBody BlogPost p) {
        boolean isAdmin = "ADMIN".equalsIgnoreCase(me.role());
        return BlogPostDto.from(blogService.update(id, p, me.id(), isAdmin));
    }

    @DeleteMapping("/blog/posts/{id}")
    public void delete(@AuthenticationPrincipal AuthUser me, @PathVariable Long id) {
        boolean isAdmin = "ADMIN".equalsIgnoreCase(me.role());
        blogService.delete(id, me.id(), isAdmin);
    }
}
