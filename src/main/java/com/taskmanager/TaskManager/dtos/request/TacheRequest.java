package com.taskmanager.TaskManager.dtos.request;

import com.taskmanager.TaskManager.utils.PrioriteTacheEnum;
import com.taskmanager.TaskManager.utils.StatutTacheEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TacheRequest {
    @NotBlank(message = "Le titre est obligatoire")
    private String titre;
    
    private String description;
    
    @NotNull(message = "Le statut est obligatoire")
    private StatutTacheEnum statut;
    
    private PrioriteTacheEnum priorite;
    
    private LocalDateTime dateEcheance;
}
