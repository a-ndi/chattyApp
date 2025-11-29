package com.work.chatty.controller;

import com.work.chatty.dto.UserResponse;
import com.work.chatty.model.User;
import com.work.chatty.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/friends")
public class FriendController {


        @Autowired
        FriendService friendService;

        @PostMapping("/add")
        public String addFriend(@RequestParam Long userId, @RequestParam Long friendId) {
            return friendService.addFriend(userId, friendId);
        }

        @GetMapping("/list")
        public Set<UserResponse> listFriends(@RequestParam Long userId) {
            return friendService.getFriends(userId).stream()
                    .map(this::toUserResponse)
                    .collect(Collectors.toSet());
        }

        private UserResponse toUserResponse(User user) {
            return new UserResponse(user.getId(), user.getUsername());
        }

}
