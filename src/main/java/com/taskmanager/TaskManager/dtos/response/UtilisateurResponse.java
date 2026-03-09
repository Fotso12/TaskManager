package com.taskmanager.TaskManager.dtos.response;

import lombok.Data;

@Data
public class UtilisateurResponse {
    private Integer idUser;
    private String nom;
    private String prenom;
    private String email;
    private String role;
}
