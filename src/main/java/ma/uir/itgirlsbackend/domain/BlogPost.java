package ma.uir.itgirlsbackend.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name="blog_posts")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BlogPost {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String category;
    private String title;

    @Column(length = 2000)
    private String excerpt;

    private LocalDate publishDate;
    private Integer readTimeMins;

    private Boolean featured;

    @Column(name = "author_name")
    private String authorName;
}
