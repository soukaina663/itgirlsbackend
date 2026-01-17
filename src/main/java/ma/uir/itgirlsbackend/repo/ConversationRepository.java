package ma.uir.itgirlsbackend.repo;

import ma.uir.itgirlsbackend.domain.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    @Query("""
        select distinct c
        from Conversation c
        join ConversationParticipant p on p.conversation = c
        where p.userId = :userId
        order by c.updatedAt desc
    """)
    List<Conversation> findForUser(Long userId);

    @Query("""
        select c
        from Conversation c
        where c.id in (
            select p1.conversation.id
            from ConversationParticipant p1
            join ConversationParticipant p2 on p2.conversation = p1.conversation
            where p1.userId = :idA and p2.userId = :idB
            group by p1.conversation.id
            having count(p1.conversation.id) = 2
        )
    """)
    Optional<Conversation> findDirectBetween(Long idA, Long idB);
}
