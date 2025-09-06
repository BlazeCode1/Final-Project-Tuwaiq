package org.example.finalprojecttuwaiq.Controller;

import lombok.RequiredArgsConstructor;
import org.example.finalprojecttuwaiq.Api.AuthToken;
import org.example.finalprojecttuwaiq.DTO.LoginRequestDTO;
import org.example.finalprojecttuwaiq.Service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequestDTO){

        return ResponseEntity.ok(new AuthToken(authService.login(loginRequestDTO.getUsername(),loginRequestDTO.getPassword())));
    }
}
