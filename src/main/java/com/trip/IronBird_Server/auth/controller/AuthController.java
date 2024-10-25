package com.trip.IronBird_Server.auth.controller;

import com.trip.IronBird_Server.auth.service.AuthService;
import com.trip.IronBird_Server.jwt.util.JwtUtil;
import com.trip.IronBird_Server.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;


    @PostMapping("/api/auth/signin")
    public String authenticateUser(@RequestBody User user){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        user.getPassword()
                )
        );;
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return jwtUtil.generateToken(userDetails.getUsername());

    }

//    @PostMapping("/signup")
//    public String registerUser(@RequestBody User user) {
//        if (userRepository.existsByUsername(user.getUsername())) {
//            return "Error: Username is already taken!";
//        }
//        // Create new user's account
//        User newUser = new User(
//                user.getUsername(),
//                encoder.encode(user.getPassword()),
//                null
//                );
//        userRepository.save(newUser);
//        return "User registered successfully!";
//    }



}
