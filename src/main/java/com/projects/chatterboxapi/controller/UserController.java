package com.projects.chatterboxapi.controller;

import com.google.gson.Gson;
import com.projects.chatterboxapi.entity.User;
import com.projects.chatterboxapi.repository.UserRepository;
import com.projects.chatterboxapi.utils.AppConstants;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private UserRepository userRepository;

    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, User>> get(@RequestParam(value = "ids") String idsString) {
        Set<Integer> ids = parseIds(idsString);

        Map<String, User> usersMap = new HashMap<>();

        for (Integer id : ids) {
            User user = userRepository.getUserById(id);
            if (user == null){
                LOGGER.debug("User not found by id: "+id);
                return new ResponseEntity<>(new HashMap<>(), HttpStatus.BAD_REQUEST);
            }
            usersMap.put(String.valueOf(user.getId()), user);
        }

        return new ResponseEntity<>(usersMap, HttpStatus.OK);
    }

    private Set<Integer> parseIds(String idsString) {
        return Arrays.stream(idsString.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toSet());
    }

    @RequestMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getMe(Model model, HttpSession session) {
        String user = (String) session.getAttribute(AppConstants.USER_ATTR_NAME);
        if (user == null){
            LOGGER.debug("User not found in session by attribute: "+AppConstants.USER_ATTR_NAME);
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
        Gson gson = new Gson();
        return new ResponseEntity<>(gson.fromJson(user, User.class), HttpStatus.OK);
    }

    @RequestMapping(value = "/online", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, User>> getOnline() {
        Map<String, User> usersMap = new HashMap<>();
        Set<Integer> onlineIds = userRepository.getOnlineUserIds();
        if (onlineIds == null){
            LOGGER.debug("No online users found!");
            return new ResponseEntity<>(new HashMap<>(), HttpStatus.OK);
        }

        for (Integer onlineId : onlineIds) {
            User user = userRepository.getUserById(onlineId);
            if (user == null){
                LOGGER.debug("User not found by id: "+onlineId);
                return new ResponseEntity<>(new HashMap<>(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            usersMap.put(String.valueOf(user.getId()), user);
        }

        return new ResponseEntity<>(usersMap, HttpStatus.OK);
    }
}
