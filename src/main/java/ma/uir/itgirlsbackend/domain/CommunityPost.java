package ma.uir.itgirlsbackend.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="community_posts")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CommunityPost {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(name = "author_name")
    private String authorName;

    private String imageUrl;

    @Column(name = "author_user_id")
    private Long authorUserId;

}
