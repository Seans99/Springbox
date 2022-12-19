package com.project.springbox.controllers;

import com.project.springbox.dtos.UserCreation;
import com.project.springbox.dtos.UserPayload;
import com.project.springbox.security.UserObject;
import com.project.springbox.services.UserService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/register")
    public UserPayload registerUser(
            @RequestBody UserCreation creation)
    {
        var user = userService.registerUser(
                creation.getUsername(),
                creation.getPassword(),
                creation.isAdmin()
        );
        return UserPayload.fromUser(user);
    }

    @GetMapping("/info")
    public UserPayload info(
            @AuthenticationPrincipal UserObject user
    ) {
        return UserPayload.fromUser(user.getUser());
    }

    @Getter
    @Setter
    public static class RegisterUser {
        private String userName;
    }
}
