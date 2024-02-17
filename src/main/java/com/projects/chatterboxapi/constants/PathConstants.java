package com.projects.chatterboxapi.constants;

public class PathConstants {

    public static final String API_V1_AUTH = "/api/v1/auth";
    public static final String ME = "/me";
    public static final String URL = "/url";
    public static final String CALLBACK = "/callback";
    public static final String LOGOUT = "/logout";

    public static final String API_V1_MESSAGES = "/api/v1/messages";
    public static final String CHAT = "/chat";
    public static final String SENDER_ID_RECIPIENT_ID_COUNT = API_V1_MESSAGES + "/{senderId}/{recipientId}/count";
    public static final String SENDER_ID_RECIPIENT_ID = API_V1_MESSAGES + "/{senderId}/{recipientId}";
    public static final String ID = API_V1_MESSAGES + "/{id}";
    public static final String QUEUE_MESSAGES = "/queue/messages";

    public static final String API_V1_USERS = "/api/v1/users";

    public static final String WS = "/ws";
    public static final String USER = "/user";
    public static final String APP = "/app";




}
