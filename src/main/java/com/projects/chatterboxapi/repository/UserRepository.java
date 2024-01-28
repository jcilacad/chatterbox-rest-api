package com.projects.chatterboxapi.repository;

import com.projects.chatterboxapi.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
}
