package com.taskmanager.TaskManager.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private UtilisateurResponse utilisateur;
}
