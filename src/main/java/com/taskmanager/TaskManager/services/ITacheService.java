package com.taskmanager.TaskManager.services;

import com.taskmanager.TaskManager.dtos.request.TacheRequest;
import com.taskmanager.TaskManager.dtos.response.TacheResponse;
import org.springframework.data.domain.Page;

public interface ITacheService {
    TacheResponse creer(TacheRequest request, String emailUtilisateur);
    TacheResponse modifier(Integer idTache, TacheRequest request, String emailUtilisateur);
    void supprimer(Integer idTache, String emailUtilisateur);
    TacheResponse obtenirParId(Integer idTache, String emailUtilisateur);
    Page<TacheResponse> obtenirToutes(int page, int size, String emailUtilisateur);
}
