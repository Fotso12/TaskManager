# Task Manager API - Spring Boot

Ce projet est une API REST de gestion de tâches développée avec Spring Boot, respectant les principes SOLID, Clean Code et intégrant une démarche DevSecOps complète.

## 📋 Table des Matières
- [Conceptions & Modélisation](#conceptions--modélisation)
- [Conventions de Développement](#conventions-de-développement)
- [Architecture Applicative](#architecture-applicative)
- [CI/CD & DevSecOps](#cicd--devsecops)
- [Installation & Utilisation](#installation--utilisation)

---

## 🏗️ Conceptions & Modélisation

### 1. Reformulation du Besoin
**Acteurs :** 
- Utilisateur (USER) : Peut créer, lire, mettre à jour et supprimer ses propres tâches.
- Administrateur (ADMIN) : Possède tous les droits de l'utilisateur + gestion globale (si applicable).

**Fonctionnalités :**
- Authentification JWT (Inscription / Connexion).
- CRUD complet des tâches.
- Validation des données.
- Pagination et gestion des erreurs globale.

**Contraintes :**
- Séparation des responsabilités.
- Couverture de tests > 60%.
- Pipeline CI/CD automatisé (GitHub Actions).
- Analyse SonarCloud et Scan de sécurité (Trivy, Gitleaks).

### 2. Modélisation des Données
Basée sur le script SQL fourni :
- **UTILISATEUR** : IDUSER (PK), NOM, PRENOM, EMAIL (Unique), MOTDEPASSE, ROLE, STATUT, DATECREATION, DATEMODIFICATION.
- **TACHE** : IDTACHE (PK), IDUSER (FK), TITRE, DESCRIPTION, STATUT, PRIORITE, DATEECHEANCE, DATECREATION, DATEMODIFICATION.

---

## 📏 Conventions de Développement

### Convention de Nommage
- **Java (Code)** : `CamelCase` pour les classes, `lowerCamelCase` pour les méthodes et variables.
- **Constantes / Enums** : `SCREAMING_SNAKE_CASE`.
- **Base de Données** : `SNAKE_CASE` pour les tables et colonnes (ex: `ID_USER`).
- **Endpoints REST** : `spinal-case` (kebab-case) (ex: `/api/v1/taches-utilisateur`).

### Convention Git (Commits & Branches)
- **Branches** :
    - `main` : Production, code stable.
    - `develop` : Intégration des fonctionnalités.
    - `feature/nom-feature` : Développement de nouvelles fonctionnalités.
    - `hotfix/nom-fix` : Corrections urgentes.
- **Messages de Commit** :
    - `feat: ...` (nouvelle fonctionnalité)
    - `fix: ...` (correction de bug)
    - `docs: ...` (documentation)
    - `refactor: ...` (amélioration de code sans changement fonctionnel)

### Push & Pull Request
- Chaque fonctionnalité doit faire l'objet d'une Pull Request (PR) vers `develop`.
- Le pipeline CI doit passer avec succès (Build + Tests + Sonar) avant le merge.

---

## 🧩 Architecture Applicative
L'application suit une structure multicouche :
- **Controllers** : Gestion des requêtes HTTP et réponses via DTOs.
- **Services (Interfaces & Impl)** : Logique métier et orchestration.
- **Repositories** : Accès aux données (Spring Data JPA).
- **Entities** : Modèles de données (JPA).
- **DTOs & Mappers** : Transformation entre entités et objets de transfert.
- **Security** : Configuration JWT et Spring Security.

---

## 🚀 CI/CD & DevSecOps
Le pipeline GitHub Actions est structuré comme suit :
1. **Build & Lint** : Compilation Maven et vérification syntaxique.
2. **Tests** : Exécution des tests unitaires et feature.
3. **SonarCloud** : Analyse de la qualité du code et couverture.
4. **Security Scan** :
    - Dependency Check (SCA).
    - Gitleaks (Secrets detection).
    - Trivy (Docker image scan).
5. **Docker** : Build et Push sur le registre.
6. **Deploy** : Déploiement via Docker Compose.

---

## 🛠️ Guide d'Utilisation (À venir)
*Instructions pour lancer le projet localement et via Docker.*
