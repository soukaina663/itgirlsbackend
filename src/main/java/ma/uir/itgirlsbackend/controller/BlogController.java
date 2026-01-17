package ma.uir.itgirlsbackend.controller;

import ma.uir.itgirlsbackend.dto.BlogPostDto;
import ma.uir.itgirlsbackend.service.BlogService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/blog")
public class BlogController {

    private final BlogService blogService;

    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    // GET /api/public/blog/posts?category=Data&q=react
    @GetMapping("/posts")
    public List<BlogPostDto> posts(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String q
    ) {
        return blogService.list(category, q).stream().map(BlogPostDto::from).toList();
    }

    // GET /api/public/blog/featured
    @GetMapping("/featured")
    public BlogPostDto featured() {
        var post = blogService.featuredOrNull();
        return post == null ? null : BlogPostDto.from(post);
    }
}

