package com.taskmanager.TaskManager.dtos.response;

import com.taskmanager.TaskManager.utils.PrioriteTacheEnum;
import com.taskmanager.TaskManager.utils.StatutTacheEnum;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TacheResponse {
    private Integer idTache;
    private String titre;
    private String description;
    private StatutTacheEnum statut;
    private PrioriteTacheEnum priorite;
    private LocalDateTime dateEcheance;
    private LocalDateTime dateCreation;
    private LocalDateTime dateModification;
}
