package com.work.chatty.controller;
import com.work.chatty.dto.MessageResponse;
import com.work.chatty.dto.UserResponse;
import com.work.chatty.model.Message;
import com.work.chatty.model.User;
import com.work.chatty.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping("/send")
    public MessageResponse sendMessage(@RequestParam Long senderId,
                                       @RequestParam Long receiverId,
                                       @RequestParam String content){
        Message message = messageService.send(senderId, receiverId, content);
        return toMessageResponse(message);
    }

    @GetMapping("/history")
    public List<MessageResponse> getChatHistory(@RequestParam Long userId1,
                                                @RequestParam Long userId2) {
        return messageService.getChatHistory(userId1, userId2).stream()
                .map(this::toMessageResponse)
                .collect(Collectors.toList());
    }

    private MessageResponse toMessageResponse(Message message) {
        MessageResponse response = new MessageResponse();
        response.setId(message.getId());
        response.setSender(toUserResponse(message.getSender()));
        response.setReceiver(toUserResponse(message.getReceiver()));
        response.setContent(message.getContent());
        response.setTimestamp(message.getTimestamp());
        return response;
    }

    private UserResponse toUserResponse(User user) {
        return new UserResponse(user.getId(), user.getUsername());
    }
}
