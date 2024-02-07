package com.projects.chatterboxapi.repository;

import com.projects.chatterboxapi.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByEmail(String email);

    List<User> findAllByEmailNotContaining(String email);

    List<User> findByNameContainingAndEmailNotContainingAllIgnoreCase(String name, String email);
}
