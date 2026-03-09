package com.taskmanager.TaskManager.services.impl;

import com.taskmanager.TaskManager.dtos.request.TacheRequest;
import com.taskmanager.TaskManager.dtos.response.TacheResponse;
import com.taskmanager.TaskManager.entities.Tache;
import com.taskmanager.TaskManager.entities.Utilisateur;
import com.taskmanager.TaskManager.exception.ResourceNotFoundException;
import com.taskmanager.TaskManager.mappers.TacheMapper;
import com.taskmanager.TaskManager.repositories.TacheRepository;
import com.taskmanager.TaskManager.repositories.UtilisateurRepository;
import com.taskmanager.TaskManager.services.ITacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TacheServiceImpl implements ITacheService {

    private final TacheRepository tacheRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final TacheMapper tacheMapper;

    private Utilisateur getUtilisateurCourant(String email) {
        return utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));
    }

    @Override
    public TacheResponse creer(TacheRequest request, String emailUtilisateur) {
        Utilisateur utilisateur = getUtilisateurCourant(emailUtilisateur);
        
        Tache tache = tacheMapper.toEntity(request);
        tache.setUtilisateur(utilisateur);
        
        Tache savedTache = tacheRepository.save(tache);
        return tacheMapper.toResponse(savedTache);
    }

    @Override
    public TacheResponse modifier(Integer idTache, TacheRequest request, String emailUtilisateur) {
        Utilisateur utilisateur = getUtilisateurCourant(emailUtilisateur);
        
        Tache tache = tacheRepository.findByIdTacheAndUtilisateurIdUser(idTache, utilisateur.getIdUser())
                .orElseThrow(() -> new ResourceNotFoundException("Tâche introuvable ou vous n'avez pas les droits."));

        tache.setTitre(request.getTitre());
        tache.setDescription(request.getDescription());
        tache.setStatut(request.getStatut());
        tache.setPriorite(request.getPriorite());
        tache.setDateEcheance(request.getDateEcheance());

        Tache updatedTache = tacheRepository.save(tache);
        return tacheMapper.toResponse(updatedTache);
    }

    @Override
    public void supprimer(Integer idTache, String emailUtilisateur) {
        Utilisateur utilisateur = getUtilisateurCourant(emailUtilisateur);
        
        Tache tache = tacheRepository.findByIdTacheAndUtilisateurIdUser(idTache, utilisateur.getIdUser())
                .orElseThrow(() -> new ResourceNotFoundException("Tâche introuvable ou vous n'avez pas les droits."));

        tacheRepository.delete(tache);
    }

    @Override
    public TacheResponse obtenirParId(Integer idTache, String emailUtilisateur) {
        Utilisateur utilisateur = getUtilisateurCourant(emailUtilisateur);
        
        Tache tache = tacheRepository.findByIdTacheAndUtilisateurIdUser(idTache, utilisateur.getIdUser())
                .orElseThrow(() -> new ResourceNotFoundException("Tâche introuvable ou vous n'avez pas les droits."));

        return tacheMapper.toResponse(tache);
    }

    @Override
    public Page<TacheResponse> obtenirToutes(int page, int size, String emailUtilisateur) {
        Utilisateur utilisateur = getUtilisateurCourant(emailUtilisateur);
        Pageable pageable = PageRequest.of(page, size);
        
        Page<Tache> taches = tacheRepository.findByUtilisateurIdUser(utilisateur.getIdUser(), pageable);
        return taches.map(tacheMapper::toResponse);
    }
}
