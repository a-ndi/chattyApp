package com.work.chatty.service;

import com.work.chatty.model.Message;
import com.work.chatty.model.User;
import com.work.chatty.repository.MessageRepository;
import com.work.chatty.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Transactional
    public Message send(Long senderId, Long receiverId, String content) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender with id " + senderId + " not found"));
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver with id " + receiverId + " not found"));

        Message msg = new Message();
        msg.setSender(sender);
        msg.setReceiver(receiver);
        msg.setContent(content);
        msg.setTimestamp(LocalDateTime.now());

        return messageRepository.save(msg);
    }

    @Transactional(readOnly = true)
    public List<Message> getChatHistory(Long userId1, Long userId2) {
        // Get messages in both directions (user1->user2 and user2->user1)
        List<Message> messages1to2 = messageRepository.findBySenderIdAndReceiverIdOrderByTimestampAsc(userId1, userId2);
        List<Message> messages2to1 = messageRepository.findBySenderIdAndReceiverIdOrderByTimestampAsc(userId2, userId1);

        // Combine and sort by timestamp
        List<Message> allMessages = new ArrayList<>();
        allMessages.addAll(messages1to2);
        allMessages.addAll(messages2to1);

        return allMessages.stream()
                .sorted(Comparator.comparing(Message::getTimestamp))
                .collect(Collectors.toList());
    }
}
