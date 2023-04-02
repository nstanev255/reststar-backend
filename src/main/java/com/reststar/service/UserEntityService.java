package com.reststar.service;

import com.reststar.entity.UserEntity;
import com.reststar.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserEntityService {
    @Autowired
    private UserEntityRepository repository;

    public UserEntity findById(Long userId) {
        Optional<UserEntity> userEntity = repository.findById(userId);

        return userEntity.orElse(null);
    }

    public UserEntity findByIdAndThrow(Long userId) {
        UserEntity user = this.findById(userId);
        if (user == null) {
            throw new RuntimeException("User does not exit");
        }

        return user;
    }

}
