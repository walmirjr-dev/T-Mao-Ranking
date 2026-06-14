package com.walmir.tmaoranking.service;

import com.walmir.tmaoranking.domain.User;
import com.walmir.tmaoranking.dto.request.UserRequest;
import com.walmir.tmaoranking.dto.response.UserResponse;
import com.walmir.tmaoranking.exception.DatabaseException;
import com.walmir.tmaoranking.exception.ResourceNotFoundException;
import com.walmir.tmaoranking.repository.UserRepository;
import com.walmir.tmaoranking.security.AuthenticatedUserService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticatedUserService authenticatedUserService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticatedUserService authenticatedUserService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticatedUserService = authenticatedUserService;
    }

    public UserResponse insert(UserRequest request) {
        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        return UserResponse.from(userRepository.save(user));
    }
    public List<UserResponse> findAll() {
        return userRepository.findAll().stream()
                .map(UserResponse::from)
                .toList();
    }

    public UserResponse findById(Long id) {
        authenticatedUserService.checkOwnership(id);
        return UserResponse.from(userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id)));
    }

    public UserResponse update(Long id, UserRequest request) {
        authenticatedUserService.checkOwnership(id);
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        existing.setName(request.name());
        existing.setEmail(request.email());
        existing.setPassword(passwordEncoder.encode(request.password()));
        return UserResponse.from(userRepository.save(existing));
    }

    public void delete(Long id) {
        authenticatedUserService.checkOwnership(id);
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException(id);
        }
        try {
            userRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Database integrity violation: " + e.getMessage());
        }
    }
}