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

    @PostMapping("/public/messaging/conversations")
    public Long createOrGet(@Valid @RequestBody CreateConversationRequest req) {
        return messageService.createOrGetDirectConversation(req);
    }

    @GetMapping("/messaging/conversations")
    public List<ConversationSummaryDto> inbox(@AuthenticationPrincipal AuthUser me) {
        return messageService.listInbox(me.id());
    }

    @GetMapping("/messaging/conversations/{conversationId}/messages")
    public List<MessageDto> messages(@PathVariable Long conversationId) {
        return messageService.getMessages(conversationId);
    }

    @PostMapping("/messaging/conversations/{conversationId}/messages")
    public MessageDto send(@PathVariable Long conversationId, @Valid @RequestBody SendMessageRequest req) {
        return messageService.sendMessage(conversationId, req);
    }

    @PatchMapping("/messaging/conversations/{conversationId}/read")
    public void markRead(@PathVariable Long conversationId, @AuthenticationPrincipal AuthUser me) {
        messageService.markConversationRead(conversationId, me.id());
    }
}
