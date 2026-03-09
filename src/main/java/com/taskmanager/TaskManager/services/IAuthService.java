package com.taskmanager.TaskManager.services;

import com.taskmanager.TaskManager.dtos.request.AuthRequest;
import com.taskmanager.TaskManager.dtos.request.InscriptionRequest;
import com.taskmanager.TaskManager.dtos.response.AuthResponse;
import com.taskmanager.TaskManager.dtos.response.UtilisateurResponse;

public interface IAuthService {
    UtilisateurResponse inscrire(InscriptionRequest request);
    AuthResponse authentifier(AuthRequest request);
}
