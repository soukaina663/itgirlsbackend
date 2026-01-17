package ma.uir.itgirlsbackend.dto;

import ma.uir.itgirlsbackend.domain.BlogPost;

import java.time.LocalDate;

public record BlogPostDto(
        Long id,
        String category,
        String title,
        String excerpt,
        LocalDate publishDate,
        Integer readTimeMins,
        Boolean featured,
        String authorName
) {
    public static BlogPostDto from(BlogPost p) {
        return new BlogPostDto(
                p.getId(),
                p.getCategory(),
                p.getTitle(),
                p.getExcerpt(),
                p.getPublishDate(),
                p.getReadTimeMins(),
                p.getFeatured(),
                p.getAuthorName()
        );
    }
}
