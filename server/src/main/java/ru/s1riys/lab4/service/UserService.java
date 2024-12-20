package ru.s1riys.lab4.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ru.s1riys.lab4.domain.entity.User;
import ru.s1riys.lab4.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User create(User user) {
        return userRepository.save(user);
    }

    public User getByUsername(String username) {
        return userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username + " not found"));
    }

    public User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(username);
    }

    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }
}
