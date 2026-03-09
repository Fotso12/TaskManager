package com.taskmanager.TaskManager.repositories;

import com.taskmanager.TaskManager.entities.Tache;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TacheRepository extends JpaRepository<Tache, Integer> {
    Page<Tache> findByUtilisateurIdUser(Integer idUser, Pageable pageable);
    Optional<Tache> findByIdTacheAndUtilisateurIdUser(Integer idTache, Integer idUser);
}
