package com.project.springbox.dtos;

import com.project.springbox.data.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class UserPayload {

    private final Integer id;

    private String username;
    private boolean admin;

    public static UserPayload fromUser(User user) {
        var payload = new UserPayload(user.getId());
        payload.setAdmin(user.isAdmin());
        payload.setUsername(user.getName());

        return payload;
    }

}
