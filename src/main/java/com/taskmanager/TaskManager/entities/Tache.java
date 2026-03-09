package com.taskmanager.TaskManager.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.taskmanager.TaskManager.utils.StatutTacheEnum;
import com.taskmanager.TaskManager.utils.PrioriteTacheEnum;

import java.time.LocalDateTime;

@Entity
@Table(name = "TACHE")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tache {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDTACHE")
    private Integer idTache;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDUSER", nullable = false)
    private Utilisateur utilisateur;

    @Column(name = "TITRE", nullable = false, length = 100)
    private String titre;

    @Column(name = "DESCRIPTION", columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUT", nullable = false, length = 20)
    private StatutTacheEnum statut;

    @Enumerated(EnumType.STRING)
    @Column(name = "PRIORITE", length = 20)
    private PrioriteTacheEnum priorite;

    @Column(name = "DATEECHEANCE")
    private LocalDateTime dateEcheance;

    @CreationTimestamp
    @Column(name = "DATECREATION", updatable = false)
    private LocalDateTime dateCreation;

    @UpdateTimestamp
    @Column(name = "DATEMODIFICATION")
    private LocalDateTime dateModification;
}
