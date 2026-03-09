package com.taskmanager.TaskManager.mappers;

import com.taskmanager.TaskManager.dtos.request.TacheRequest;
import com.taskmanager.TaskManager.dtos.response.TacheResponse;
import com.taskmanager.TaskManager.entities.Tache;
import org.springframework.stereotype.Component;

@Component
public class TacheMapper {

    public TacheResponse toResponse(Tache tache) {
        if (tache == null) {
            return null;
        }

        TacheResponse response = new TacheResponse();
        response.setIdTache(tache.getIdTache());
        response.setTitre(tache.getTitre());
        response.setDescription(tache.getDescription());
        response.setStatut(tache.getStatut());
        response.setPriorite(tache.getPriorite());
        response.setDateEcheance(tache.getDateEcheance());
        response.setDateCreation(tache.getDateCreation());
        response.setDateModification(tache.getDateModification());
        
        return response;
    }

    public Tache toEntity(TacheRequest request) {
        if (request == null) {
            return null;
        }

        Tache tache = new Tache();
        tache.setTitre(request.getTitre());
        tache.setDescription(request.getDescription());
        tache.setStatut(request.getStatut());
        tache.setPriorite(request.getPriorite());
        tache.setDateEcheance(request.getDateEcheance());
        
        return tache;
    }
}
