package com.example.mySchedule.services;

import com.example.mySchedule.DTOs.AuthResponse;
import com.example.mySchedule.DTOs.LoginRequest;
import com.example.mySchedule.repositories.RepoUser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    @Autowired
    JwtService jwtService;
    @Autowired
    RepoUser repoUser;

    private final AuthenticationManager authenticationManager;

    public AuthResponse login(LoginRequest login){

        String incomingUserName=(repoUser.findByEmail(login.getUsername())).getName();

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        incomingUserName,
                        login.getPassword()
                )
        );

        UserModel myUser = userRepository.findByUsername(incomingUserName).orElseThrow();
        UserDetails user = (UserDetails) userRepository.findByUsername(incomingUserName).orElseThrow();

        String myToken = jwtService.getTokenService(user);

        return AuthResponse
                .builder()
                .userId(myUser.getId())
                .token(myToken)
                .build();
    }
}
