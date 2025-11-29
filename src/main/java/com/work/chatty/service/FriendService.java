package com.work.chatty.service;

import com.work.chatty.model.User;
import com.work.chatty.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class FriendService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public String addFriend(Long userId, Long friendId) {
        // Validation: Prevent self-friend
        if (userId.equals(friendId)) {
            throw new RuntimeException("Cannot add yourself as a friend");
        }

        // Find users
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User with id " + userId + " not found"));
        User friend = userRepository.findById(friendId)
                .orElseThrow(() -> new RuntimeException("User with id " + friendId + " not found"));

        // Validation: Check if already friends
        if (user.getFriends().contains(friend)) {
            throw new RuntimeException("User " + friend.getUsername() + " is already your friend");
        }

        // Add each other as friends (bidirectional)
        user.getFriends().add(friend);
        friend.getFriends().add(user);

        // Save both users
        userRepository.save(user);
        userRepository.save(friend);

        return "Friend " + friend.getUsername() + " added successfully";
    }

    @Transactional(readOnly = true)
    public Set<User> getFriends(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User with id " + userId + " not found"));
        return user.getFriends();
    }

}
