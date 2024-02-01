package com.projects.chatterboxapi.repository;

import com.projects.chatterboxapi.dto.request.UserDtoRequest;
import com.projects.chatterboxapi.entity.User;
import com.projects.chatterboxapi.mapper.UserMapper;
import com.projects.chatterboxapi.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class UserRepositoryTest {

    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @Value("${jwt.secret}")
    private String jwtSecret;

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

        UserDtoRequest userDtoRequest = UserDtoRequest.builder()
                .id(id)
                .name(name)
                .email(email)
                .imageUrl(imageUrl)
                .build();

        User savedUser = UserMapper.MAPPER.toEntity(userDtoRequest);
        userRepository.save(savedUser);
    }

    @Test
    void updateUser () {
        User user = userRepository.findById("108977918358293537943").orElseThrow();

        user.setName("adawdawdaw");
        user.setEmail("Jdawdaw@gmail.com");
        userRepository.save(user);
    }

    @Test
    void setActiveStatus() {
        boolean status = false;
        String email = "johnchristopherilacad27@gmail.com";
        userService.setActiveStatus(email, status);
    }

    @Test
    void sampleJwtSecret () {
        log.info("JWT Secret - {}", jwtSecret);
    }
}