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

    public static final String BASE = "/";
    public static final String PUBLIC_ALL = "/public/**";
    public static final String API_V1_AUTH_URL = "/api/v1/auth/url";
    public static final String API_V1_AUTH_CALLBACK = "/api/v1/auth/callback";
    public static final String SWAGGER_UI_ALL = "/swagger-ui/**";
    public static final String V3_API_DOCS_ALL = "/v3/api-docs/**";


}
