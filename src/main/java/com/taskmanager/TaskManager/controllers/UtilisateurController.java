package com.taskmanager.TaskManager.controllers;

import com.taskmanager.TaskManager.dtos.response.ApiResponse;
import com.taskmanager.TaskManager.dtos.response.UtilisateurResponse;
import com.taskmanager.TaskManager.services.IUtilisateurService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contrôleur responsable des opérations liées aux utilisateurs.
 * Documenté pour Swagger / OpenAPI.
 * Développeur: TAMO FOTSO Michel Darryl
 */
@RestController
@RequestMapping("/api/v1/utilisateurs")
@RequiredArgsConstructor
@Tag(name = "Utilisateurs", description = "Gestion des profils utilisateurs")
@SecurityRequirement(name = "bearerAuth")
public class UtilisateurController {

    private final IUtilisateurService utilisateurService;

    @Operation(summary = "Obtenir le profil", description = "Récupère les informations de l'utilisateur connecté")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Profil récupéré avec succès"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Non autorisé - token invalide ou manquant"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Utilisateur non trouvé")
    })
    @GetMapping("/profil")
    public ResponseEntity<ApiResponse<UtilisateurResponse>> obtenirProfil(Authentication authentication) {
        UtilisateurResponse profil = utilisateurService.obtenirProfil(authentication.getName());
        return new ResponseEntity<>(new ApiResponse<>(true, "Profil récupéré avec succès", profil), HttpStatus.OK);
    }
}
