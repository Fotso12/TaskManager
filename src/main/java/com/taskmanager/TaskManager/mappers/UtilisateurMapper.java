package com.taskmanager.TaskManager.mappers;

import com.taskmanager.TaskManager.dtos.response.UtilisateurResponse;
import com.taskmanager.TaskManager.entities.Utilisateur;
import org.springframework.stereotype.Component;

@Component
public class UtilisateurMapper {

    public UtilisateurResponse toResponse(Utilisateur utilisateur) {
        if (utilisateur == null) {
            return null;
        }
        
        UtilisateurResponse response = new UtilisateurResponse();
        response.setIdUser(utilisateur.getIdUser());
        response.setNom(utilisateur.getNom());
        response.setPrenom(utilisateur.getPrenom());
        response.setEmail(utilisateur.getEmail());
        if (utilisateur.getRole() != null) {
            response.setRole(utilisateur.getRole().name());
        }
        return response;
    }
}
