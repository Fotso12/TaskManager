package com.taskmanager.TaskManager.services;

import com.taskmanager.TaskManager.dtos.response.UtilisateurResponse;

public interface IUtilisateurService {
    UtilisateurResponse obtenirProfil(String email);
}
