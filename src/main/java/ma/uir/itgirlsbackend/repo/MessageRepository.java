package ma.uir.itgirlsbackend.repo;

import ma.uir.itgirlsbackend.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByConversationIdOrderByCreatedAtAsc(Long conversationId);

    Optional<Message> findTop1ByConversationIdOrderByCreatedAtDesc(Long conversationId);

    @Query("""
        select count(m)
        from Message m
        where m.conversation.id = :conversationId
          and m.createdAt > :since
          and m.senderId <> :userId
    """)
    long countUnread(Long conversationId, Long userId, LocalDateTime since);
}
