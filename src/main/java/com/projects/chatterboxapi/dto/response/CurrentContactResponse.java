package com.projects.chatterboxapi.dto.response;

import com.projects.chatterboxapi.dto.request.UserRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CurrentContactResponse {

    private UserRequest userRequest;
    private List<ChatMessageResponse> chatMessageResponses;
}
