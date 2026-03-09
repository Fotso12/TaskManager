package com.taskmanager.TaskManager.controllers;

import com.taskmanager.TaskManager.dtos.request.AuthRequest;
import com.taskmanager.TaskManager.dtos.request.InscriptionRequest;
import com.taskmanager.TaskManager.dtos.response.ApiResponse;
import com.taskmanager.TaskManager.dtos.response.AuthResponse;
import com.taskmanager.TaskManager.dtos.response.UtilisateurResponse;
import com.taskmanager.TaskManager.services.IAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final IAuthService authService;

    @PostMapping("/inscription")
    public ResponseEntity<ApiResponse<UtilisateurResponse>> inscrire(@Valid @RequestBody InscriptionRequest request) {
        UtilisateurResponse response = authService.inscrire(request);
        return new ResponseEntity<>(new ApiResponse<>(true, "Inscription réussie", response), HttpStatus.CREATED);
    }

    @PostMapping("/connexion")
    public ResponseEntity<ApiResponse<AuthResponse>> authentifier(@Valid @RequestBody AuthRequest request) {
        AuthResponse response = authService.authentifier(request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Authentification réussie", response));
    }
}
