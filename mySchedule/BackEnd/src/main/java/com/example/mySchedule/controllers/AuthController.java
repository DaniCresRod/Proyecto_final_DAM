package com.example.mySchedule.controllers;


import com.example.mySchedule.DTOs.AuthResponse;
import com.example.mySchedule.DTOs.LoginRequest;
import com.example.mySchedule.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request)
    {
        try{
            System.out.println("Entra en /login en AuthController");
            return ResponseEntity.ok(authService.login(request));
        }
        catch(Exception e){
            System.out.println("Excepcion en AuthController");
            System.out.println(e.getMessage());
            return null;
        }
    }

}
