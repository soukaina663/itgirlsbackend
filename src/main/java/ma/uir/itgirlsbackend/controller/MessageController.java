package ma.uir.itgirlsbackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.uir.itgirlsbackend.dto.*;
import ma.uir.itgirlsbackend.security.AuthUser;
import ma.uir.itgirlsbackend.service.MessageService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MessageController {

    private final MessageService messageService;

    @PostMapping("/messaging/conversations/direct")
    public Long createOrGet(@AuthenticationPrincipal AuthUser me,
                            @Valid @RequestBody CreateDirectConversationRequest req) {

        String myRole = normalizeRole(me.role());
        String otherRole = normalizeRole(req.otherUserRole());

        return messageService.createOrGetDirectConversation(
                me.id(), me.name(), myRole,
                req.otherUserId(), req.otherUserName(), otherRole,
                req.title()
        );
    }

    @GetMapping("/messaging/conversations")
    public List<ConversationSummaryDto> inbox(@AuthenticationPrincipal AuthUser me) {
        return messageService.listInbox(me.id());
    }

    @GetMapping("/messaging/conversations/{conversationId}/messages")
    public List<MessageDto> messages(@PathVariable Long conversationId,
                                     @AuthenticationPrincipal AuthUser me) {
        return messageService.getMessagesForUser(conversationId, me.id());
    }

    @PostMapping("/messaging/conversations/{conversationId}/messages")
    public MessageDto send(@PathVariable Long conversationId,
                           @AuthenticationPrincipal AuthUser me,
                           @Valid @RequestBody SendMessageContentRequest req) {
        return messageService.sendMessage(conversationId, me.id(), me.name(), req.content());
    }

    @PatchMapping("/messaging/conversations/{conversationId}/read")
    public void markRead(@PathVariable Long conversationId, @AuthenticationPrincipal AuthUser me) {
        messageService.markConversationRead(conversationId, me.id());
    }

    private static String normalizeRole(String role) {
        if (role == null) return "GIRL"; // défaut si absent
        String r = role.trim().toUpperCase();
        if (r.startsWith("ROLE_")) r = r.substring(5);
        // ✅ on garde GIRL tel quel
        if (r.equals("STUDENT")) return "GIRL"; // au cas où une vieille valeur arrive
        return r;
    }
}
