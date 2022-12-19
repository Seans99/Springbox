package com.project.springbox.services;

import com.project.springbox.data.User;
import com.project.springbox.repositories.UserRepository;
import com.project.springbox.security.UserObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException
    {
        var user = userRepository
                .findByName(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "A user with username '" + username + "' could not be found."
                ));

        return new UserObject(user);
    }

    public User registerUser(String username, String password, boolean admin)
    {
        var existing = userRepository.findByName(username);
        if (existing.isPresent()) {
            System.out.println("Failed to register user since name '" + username + "' already exists.");
            throw new HttpServerErrorException(HttpStatus.CONFLICT);
        }

        var user = new User(username, passwordEncoder.encode(password), admin);
        System.out.println("Successfully registered user with id '" + user.getId() + "'.");
        return userRepository.save(user);

    }

    public Optional<User> getByUsername(String username) {
        return userRepository.findByName(username);
    }

}
