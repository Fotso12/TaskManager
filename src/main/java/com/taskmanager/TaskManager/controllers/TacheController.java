package com.taskmanager.TaskManager.controllers;

import com.taskmanager.TaskManager.dtos.request.TacheRequest;
import com.taskmanager.TaskManager.dtos.response.ApiResponse;
import com.taskmanager.TaskManager.dtos.response.TacheResponse;
import com.taskmanager.TaskManager.services.ITacheService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/taches")
@RequiredArgsConstructor
public class TacheController {

    private final ITacheService tacheService;

    @PostMapping
    public ResponseEntity<ApiResponse<TacheResponse>> creerTache(
            @Valid @RequestBody TacheRequest request,
            Authentication authentication) {
        TacheResponse response = tacheService.creer(request, authentication.getName());
        return new ResponseEntity<>(new ApiResponse<>(true, "Tâche créée avec succès", response), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<TacheResponse>>> obtenirToutesLesTaches(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication) {
        Page<TacheResponse> taches = tacheService.obtenirToutes(page, size, authentication.getName());
        return ResponseEntity.ok(new ApiResponse<>(true, "Tâches récupérées avec succès", taches));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TacheResponse>> obtenirTacheParId(
            @PathVariable Integer id,
            Authentication authentication) {
        TacheResponse tache = tacheService.obtenirParId(id, authentication.getName());
        return ResponseEntity.ok(new ApiResponse<>(true, "Tâche récupérée avec succès", tache));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TacheResponse>> modifierTache(
            @PathVariable Integer id,
            @Valid @RequestBody TacheRequest request,
            Authentication authentication) {
        TacheResponse tache = tacheService.modifier(id, request, authentication.getName());
        return ResponseEntity.ok(new ApiResponse<>(true, "Tâche modifiée avec succès", tache));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> supprimerTache(
            @PathVariable Integer id,
            Authentication authentication) {
        tacheService.supprimer(id, authentication.getName());
        return ResponseEntity.ok(new ApiResponse<>(true, "Tâche supprimée avec succès", null));
    }
}
