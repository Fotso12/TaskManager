package com.taskmanager.TaskManager.services.impl;

import com.taskmanager.TaskManager.dtos.response.UtilisateurResponse;
import com.taskmanager.TaskManager.entities.Utilisateur;
import com.taskmanager.TaskManager.exception.ResourceNotFoundException;
import com.taskmanager.TaskManager.mappers.UtilisateurMapper;
import com.taskmanager.TaskManager.repositories.UtilisateurRepository;
import com.taskmanager.TaskManager.services.IUtilisateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UtilisateurServiceImpl implements IUtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    private final UtilisateurMapper utilisateurMapper;

    @Override
    public UtilisateurResponse obtenirProfil(String email) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable avec l'email: " + email));
        
        return utilisateurMapper.toResponse(utilisateur);
    }
}
