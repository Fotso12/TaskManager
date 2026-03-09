package com.taskmanager.TaskManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Point d'entrée de l'application TaskManager.
 *
 * Ce projet implémente une API REST pour la gestion de tâches avec :
 * - Authentification JWT
 * - CRUD Tâches et gestion des utilisateurs
 * - Architecture en couches (controllers, services, repositories)
 *
 * Auteur / Développeur : TAMO FOTSO Michel Darryl
 */
@SpringBootApplication
public class TaskManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskManagerApplication.class, args);
	}

}
