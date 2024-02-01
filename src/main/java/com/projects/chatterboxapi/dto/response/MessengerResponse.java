package com.projects.chatterboxapi.dto.response;

import com.projects.chatterboxapi.dto.request.UserDtoRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessengerResponse {

    private UserDtoRequest loggedInUser;
    private List<UserDtoRequest> userDtoRequests;
    private List<ChatMessageResponse> chatMessageResponses;
}
