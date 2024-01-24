package com.projects.chatterboxapi.repository;

import com.projects.chatterboxapi.entity.User;
import com.projects.chatterboxapi.utils.AppConstants;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
public class UserRepository {
    private static final Logger logger = LoggerFactory.getLogger(UserRepository.class);
    private StringRedisTemplate redisTemplate;

    public User getUserById(int userId) {
        String usernameKey = String.format(AppConstants.USER_ID_KEY, userId);
        String username = (String) redisTemplate.opsForHash().get(usernameKey, AppConstants.USERNAME_HASH_KEY);
        if (username == null) {
            logger.error(String.format("User was not found by id:%s", userId));
            return null;
        }
        boolean isOnline = redisTemplate.opsForSet().isMember(AppConstants.ONLINE_USERS_KEY, String.valueOf(userId));
        return new User(userId, username, isOnline);
    }

    public Set<Integer> getOnlineUserIds() {
        Set<String> onlineIds = redisTemplate.opsForSet().members(AppConstants.ONLINE_USERS_KEY);
        if (onlineIds == null) {
            logger.info("No online users found");
            return null;
        }
        return onlineIds.stream()
                .map(Integer::parseInt)
                .collect(Collectors.toSet());
    }

    public boolean isUserExists(String username) {
        return redisTemplate.hasKey(String.format(AppConstants.USERNAME_KEY, username));
    }

    public User getUserByName(String username) {
        if(!isUserExists(username)) {
            return null;
        }
        String userKey = redisTemplate.opsForValue().get(String.format(AppConstants.USERNAME_KEY, username));
        int userId = parseUserId(Objects.requireNonNull(userKey));
        boolean isOnline = redisTemplate.opsForSet().isMember(AppConstants.ONLINE_USERS_KEY, String.valueOf(userId));
        return new User(userId, username, isOnline);
    }

    private int parseUserId(String userKey){
        String[] userIds = userKey.split(":");
        return Integer.parseInt(userIds[userIds.length - 1]);
    }

    public void addUserToOnlineList(String userId) {
        redisTemplate.opsForSet().add(AppConstants.ONLINE_USERS_KEY, userId);
    }

    public void removeUserFromOnlineList(String userId) {
        redisTemplate.opsForSet().remove(AppConstants.ONLINE_USERS_KEY, userId);
    }
}
