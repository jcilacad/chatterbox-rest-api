package com.projects.chatterboxapi.service;

import com.projects.chatterboxapi.entity.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

public interface UserService {
    Map<String, User> get(@RequestParam(value = "ids") String idsString);
    User getMe(HttpSession session);
    Map<String, User> getOnline();
}
