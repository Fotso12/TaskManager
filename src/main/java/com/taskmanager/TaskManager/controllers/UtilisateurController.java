package com.taskmanager.TaskManager.controllers;

import com.taskmanager.TaskManager.dtos.response.ApiResponse;
import com.taskmanager.TaskManager.dtos.response.UtilisateurResponse;
import com.taskmanager.TaskManager.services.IUtilisateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/utilisateurs")
@RequiredArgsConstructor
public class UtilisateurController {

    private final IUtilisateurService utilisateurService;

    @GetMapping("/profil")
    public ResponseEntity<ApiResponse<UtilisateurResponse>> obtenirProfil(Authentication authentication) {
        UtilisateurResponse profil = utilisateurService.obtenirProfil(authentication.getName());
        return ResponseEntity.ok(new ApiResponse<>(true, "Profil récupéré avec succès", profil));
    }
}
