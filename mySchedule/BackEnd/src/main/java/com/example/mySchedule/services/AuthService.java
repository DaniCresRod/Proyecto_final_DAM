package com.example.mySchedule.services;

import com.example.mySchedule.DTOs.AuthResponse;
import com.example.mySchedule.DTOs.LoginRequest;
import com.example.mySchedule.jwt.AuthTokenFilter;
import com.example.mySchedule.models.userModel;
import com.example.mySchedule.repositories.RepoUser;
import jakarta.servlet.http.HttpServletRequest;
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
    @Autowired
    AuthTokenFilter authTokenFilter;

    private final AuthenticationManager authenticationManager;

    public AuthResponse login(LoginRequest login){

        String incomingUserName=(repoUser.findByEmail(login.getUsername())).getNif();

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        incomingUserName,
                        login.getPassword()
                )
        );

        userModel myUser = repoUser.findByUserNif(incomingUserName).orElseThrow();
        UserDetails user = repoUser.findByUserNif(incomingUserName).orElseThrow();

        String myToken = jwtService.getTokenService(user);

        return AuthResponse
                .builder()
                .userId(myUser.getId())
                .token(myToken)
                .userName(myUser.getName())
                .userRole(myUser.getRol().toString())
                .build();
    }

    public AuthResponse isAlive(HttpServletRequest request){
        final String token = authTokenFilter.getTokenFromRequest(request);
        final String username = jwtService.getUsernameFromToken(token);

        userModel myUser = repoUser.findByUserNif(username).orElseThrow();

        return AuthResponse
                .builder()
                .userId(myUser.getId())
                //.token(token)
                .userName(myUser.getName())
                .userRole(myUser.getRol().toString())
                .build();
    }
}
