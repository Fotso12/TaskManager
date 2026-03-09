package com.taskmanager.TaskManager.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.taskmanager.TaskManager.services.ITacheService;
import com.taskmanager.TaskManager.dtos.request.TacheRequest;
import com.taskmanager.TaskManager.dtos.response.TacheResponse;
import com.taskmanager.TaskManager.dtos.response.ApiResponse;

@RestController
@RequestMapping("/api/v1/taches")
@RequiredArgsConstructor
@Tag(name = "Tâches", description = "Gestion des tâches (CRUD)")
@SecurityRequirement(name = "bearerAuth")
public class TacheController {

    private final ITacheService tacheService;

    @Operation(summary = "Créer une nouvelle tâche", description = "Ajoute une tâche pour l'utilisateur authentifié")
    @PostMapping
    public ResponseEntity<ApiResponse<TacheResponse>> creerTache(
            @Valid @RequestBody TacheRequest request,
            Authentication authentication) {
        String username = authentication != null ? authentication.getName() : "";
        TacheResponse response = tacheService.creer(request, username);
        return new ResponseEntity<>(new ApiResponse<>(true, "Tâche créée avec succès", response), HttpStatus.CREATED);
    }

    @Operation(summary = "Obtenir toutes les tâches", description = "Récupère la liste paginée des tâches de l'utilisateur")
    @GetMapping
    public ResponseEntity<ApiResponse<Page<TacheResponse>>> obtenirToutesLesTaches(
            @Parameter(description = "Numéro de la page (0 par défaut)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Nombre d'éléments par page (10 par défaut)") @RequestParam(defaultValue = "10") int size,
            Authentication authentication) {
        String username = authentication != null ? authentication.getName() : "";
        Page<TacheResponse> taches = tacheService.obtenirToutes(page, size, username);
        return ResponseEntity.ok(new ApiResponse<>(true, "Tâches récupérées avec succès", taches));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TacheResponse>> obtenirTacheParId(
            @PathVariable Integer id,
            Authentication authentication) {
        String username = authentication != null ? authentication.getName() : "";
        TacheResponse tache = tacheService.obtenirParId(id, username);
        return ResponseEntity.ok(new ApiResponse<>(true, "Tâche récupérée avec succès", tache));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TacheResponse>> modifierTache(
            @PathVariable Integer id,
            @Valid @RequestBody TacheRequest request,
            Authentication authentication) {
        String username = authentication != null ? authentication.getName() : "";
        TacheResponse tache = tacheService.modifier(id, request, username);
        return ResponseEntity.ok(new ApiResponse<>(true, "Tâche modifiée avec succès", tache));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> supprimerTache(
            @PathVariable Integer id,
            Authentication authentication) {
        String username = authentication != null ? authentication.getName() : "";
        tacheService.supprimer(id, username);
        return ResponseEntity.ok(new ApiResponse<>(true, "Tâche supprimée avec succès", null));
    }
}
