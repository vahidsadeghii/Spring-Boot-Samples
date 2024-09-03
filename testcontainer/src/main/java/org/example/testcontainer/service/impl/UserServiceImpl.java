package org.example.testcontainer.service.impl;

import org.example.testcontainer.domain.User;
import org.example.testcontainer.repository.UserRepository;
import org.example.testcontainer.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<User> getAllActiveUsers() {
        return userRepository.findAll().stream().filter(User::isActive).toList();
    }
}
