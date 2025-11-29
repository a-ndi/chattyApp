package com.work.chatty.service;

import com.work.chatty.dto.LoginRequest;
import com.work.chatty.dto.RegisterRequest;
import com.work.chatty.model.User;
import com.work.chatty.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
  @Autowired
    private UserRepository userRepository;

  @Autowired
    private PasswordEncoder passwordEncoder;

  public String register(RegisterRequest req){

      if (userRepository.findByUsername(req.getUsername()).isPresent()){
          throw new RuntimeException("Username already exist");
      }

      User user = new User();
      user.setUsername(req.getUsername());
      user.setPassword(passwordEncoder.encode(req.getPassword()));

      userRepository.save(user);

      return "User successfully registered";
  }


    public User login(LoginRequest req) {

      User user = userRepository.findByUsername(req.getUsername())
              .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return user;

    }
}
