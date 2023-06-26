package com.example.watchex.service;

import com.example.watchex.domain.User;
import com.example.watchex.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired private UserRepository repository;
    public Page<User> get(int page) {
        return repository.findAll(PageRequest.of(page, 10, Sort.by("id").descending()));
    }

    public List<User> getAll() {
        return repository.findAll();
    }

    public void save(User user) {
        repository.save(user);
    }

    public User show(Integer id) throws UserPrincipalNotFoundException {
        Optional<User> result = repository.findById(id);
        if (result.isPresent()) {
            return result.get();
        }
        throw new UserPrincipalNotFoundException("User not found");
    }
    public void delete(Integer id) {
        repository.deleteById(id);
    }
}
