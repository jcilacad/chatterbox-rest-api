package com.projects.chatterboxapi.service.impl;

import com.google.gson.Gson;
import com.projects.chatterboxapi.controller.UserController;
import com.projects.chatterboxapi.entity.User;
import com.projects.chatterboxapi.repository.UserRepository;
import com.projects.chatterboxapi.service.UserService;
import com.projects.chatterboxapi.utils.AppConstants;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    private UserRepository userRepository;

    @Override
    public Map<String, User> get(String idsString) {
        Set<Integer> ids = parseIds(idsString);

        Map<String, User> usersMap = new HashMap<>();

        for (Integer id : ids) {
            User user = userRepository.getUserById(id);
            if (user == null){
                LOGGER.debug("User not found by id: "+id);
                // TODO: Create a custom exception (Bad Request)
                throw new RuntimeException("Bad Request");
            }
            usersMap.put(String.valueOf(user.getId()), user);
        }

        return usersMap;
    }

    @Override
    public User getMe(HttpSession session) {
        String user = (String) session.getAttribute(AppConstants.USER_ATTR_NAME);
        if (user == null){
            LOGGER.debug("User not found in session by attribute: "+AppConstants.USER_ATTR_NAME);
            return null;
        }

        Gson gson = new Gson();
        return gson.fromJson(user, User.class);
    }

    @Override
    public Map<String, User> getOnline() {
        Map<String, User> usersMap = new HashMap<>();
        Set<Integer> onlineIds = userRepository.getOnlineUserIds();
        if (onlineIds == null){
            LOGGER.debug("No online users found!");
            return new HashMap<>();
        }

        for (Integer onlineId : onlineIds) {
            User user = userRepository.getUserById(onlineId);
            if (user == null){
                LOGGER.debug("User not found by id: "+onlineId);
                // TODO: Create a custom error exception (Internal Server Error)
                throw new RuntimeException("Internal Server Error");
            }
            usersMap.put(String.valueOf(user.getId()), user);
        }

        return usersMap;
    }

    private Set<Integer> parseIds(String idsString) {
        return Arrays.stream(idsString.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toSet());
    }
}
