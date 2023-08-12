package com.example.watchex.service.impl;

import com.example.watchex.entity.User;
import com.example.watchex.repository.UserRepository;
import com.example.watchex.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends GenericServiceImpl<User, Integer> implements UserService {

    @Autowired
    private UserRepository repository;

    public Page<User> get(int page) {
        return repository.findAll(PageRequest.of(page, 10, Sort.by("id").descending()));
    }

    @Override
    public User show(Integer id) throws UserPrincipalNotFoundException {
        Optional<User> result = repository.findById(id);
        if (result.isPresent()) {
            return result.get();
        }
        throw new UserPrincipalNotFoundException("User not found");
    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email) > 0;
    }

    @Override
    public User findByEmail(String email) {
        return repository.findByEmail(email);
    }
}
