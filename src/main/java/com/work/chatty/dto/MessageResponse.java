package com.work.chatty.dto;

import java.time.LocalDateTime;

public class MessageResponse {
    private Long id;
    private UserResponse sender;
    private UserResponse receiver;
    private String content;
    private LocalDateTime timestamp;

    public MessageResponse() {
    }

    public MessageResponse(Long id, UserResponse sender, UserResponse receiver, String content, LocalDateTime timestamp) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserResponse getSender() {
        return sender;
    }

    public void setSender(UserResponse sender) {
        this.sender = sender;
    }

    public UserResponse getReceiver() {
        return receiver;
    }

    public void setReceiver(UserResponse receiver) {
        this.receiver = receiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}


