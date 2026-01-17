package ma.uir.itgirlsbackend.dto;

import ma.uir.itgirlsbackend.domain.CommunityPost;

public record CommunityPostDto(
        Long id,
        String title,
        String authorName,
        String imageUrl
) {
    public static CommunityPostDto from(CommunityPost p) {
        return new CommunityPostDto(
                p.getId(),
                p.getTitle(),
                p.getAuthorName(),
                p.getImageUrl()
        );
    }
}
