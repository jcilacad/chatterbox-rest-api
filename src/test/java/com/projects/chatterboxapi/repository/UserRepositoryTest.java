package com.projects.chatterboxapi.repository;

import com.projects.chatterboxapi.dto.UserDto;
import com.projects.chatterboxapi.entity.User;
import com.projects.chatterboxapi.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserRepositoryTest {

    private UserRepository userRepository;

    @Autowired
    public UserRepositoryTest(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Test
    void saveUser() {
        String id = "121212";
        String name = "John Ilacad";
        String email = "jcdilacad2020@plm.edu.ph";
        String imageUrl = "https://facebookk.com";

        UserDto userDto = UserDto.builder()
                .id(id)
                .name(name)
                .email(email)
                .imageUrl(imageUrl)
                .build();

        User savedUser = UserMapper.MAPPER.toEntity(userDto);
        userRepository.save(savedUser);
    }
}