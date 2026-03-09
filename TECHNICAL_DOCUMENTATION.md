# Documentation technique — TaskManager

- **Projet**: TaskManager
- **Auteur / Développeur**: TAMO FOTSO Michel Darryl
- **Date**: 2026-03-09

## 1. Vue d'ensemble
API REST pour la gestion de tâches (création, modification, suppression, authentification). Basée sur Spring Boot 3.x, JPA, et JWT pour l'authentification.

## 2. Structure du projet (emplacement principal)
- `src/main/java/com/taskmanager/TaskManager/` : code source principal
  - `config/` : configuration Spring (CORS, sécurité, JWT)
  - `controllers/` : contrôleurs REST (`AuthController`, `TacheController`, `UtilisateurController`)
  - `dtos/` : DTOs request/response
  - `entities/` : entités JPA (`Tache`, `Utilisateur`)
  - `exception/` : gestion des exceptions personnalisées
  - `mappers/` : mappers entre entités et DTOs
  - `repositories/` : interfaces JPA
  - `services/` : interfaces et implémentations métier
  - `utils/` : enums utilitaires (priorité, rôle, statut)
- `src/test/java/...` : tests unitaires et d'intégration

## 3. Points d'entrée
- Classe principale: `TaskManagerApplication.java`
- Contrôleurs exposés (emplacements):
  - `AuthController` — endpoints d'authentification (login, register)
  - `TacheController` — endpoints CRUD pour les tâches
  - `UtilisateurController` — endpoints utilisateur

(Consulter les sources dans `src/main/java/com/taskmanager/TaskManager/controllers` pour la liste exacte des routes et méthodes.)

## 4. Dépendances & plugins notables (extraits de `pom.xml`)
- Spring Boot starters: `spring-boot-starter-webmvc`, `spring-boot-starter-data-jpa`, `spring-boot-starter-security`, `spring-boot-starter-validation`.
- JWT: `io.jsonwebtoken:jjwt` (api/impl/jackson)
- OpenAPI/Swagger: `springdoc-openapi-starter-webmvc-ui`
- Tests: `spring-boot-starter-test`, `spring-security-test`, `h2` (test runtime)
- Outil de couverture: `org.jacoco:jacoco-maven-plugin` (version 0.8.11)

## 5. Build / Synchronisation (ce que j'ai exécuté)
J'ai tenté une synchronisation/build Maven en désactivant la génération de rapport JaCoCo.
Commande exécutée (PowerShell) :

```
cd "C:\Users\Darryl\Desktop\EXAM\TaskManager\TaskManager"
.\mvnw.cmd -Djacoco.skip=true clean install
```

- L'option `-Djacoco.skip=true` demande au plugin JaCoCo de ne pas activer l'agent ni de générer le rapport.

## 6. Résultat du build (problème rencontré)
Le build a échoué avec l'erreur suivante:
- `dependencies.dependency.version for org.springframework.boot:spring-boot-starter-webmvc:jar is missing`.

Cause probable et correctif suggéré :
- Dans Spring Boot, l'artifact standard pour exposer des endpoints REST est généralement `spring-boot-starter-web` (qui contient Spring MVC). L'utilisation de `spring-boot-starter-webmvc` pourrait ne pas être disponible dans la BOM du parent et provoquer l'erreur.
- Solutions possibles:
  - Remplacer `spring-boot-starter-webmvc` par `spring-boot-starter-web` dans le `pom.xml`.
  - Ou ajouter explicitement une version pour `spring-boot-starter-webmvc` (moins recommandé si on utilise le parent `spring-boot-starter-parent`).

Exemple de correction recommandée dans `pom.xml` :
```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

Après correction, relancer :
```
cd "C:\Users\Darryl\Desktop\EXAM\TaskManager\TaskManager"
.\mvnw.cmd -Djacoco.skip=true clean install
```

## 7. Commandes utiles
- Build (sans JaCoCo) :
```
cd "C:\Users\Darryl\Desktop\EXAM\TaskManager\TaskManager"
.\mvnw.cmd -Djacoco.skip=true clean install
```
- Build complet (avec couverture) :
```
cd "C:\Users\Darryl\Desktop\EXAM\TaskManager\TaskManager"
.\mvnw.cmd clean install
```
- Exécuter l'application :
```
cd "C:\Users\Darryl\Desktop\EXAM\TaskManager\TaskManager"
.\mvnw.cmd spring-boot:run
```

## 8. Tests
- Les tests sont présents sous `src/test/java/...`.
- Pour exécuter uniquement les tests : `mvn test` (ou `.
mvnw.cmd test`).

## 9. Suggestions et prochaines étapes
- Corriger la dépendance `spring-boot-starter-webmvc` (voir section 6) puis relancer le build.
- Si tu veux que je corrige automatiquement le `pom.xml` (remplacer par `spring-boot-starter-web`) et relance le build, je peux le faire sur demande.
- Si tu veux que j'ajoute un profil Maven pour désactiver la couverture proprement, je peux proposer une configuration `profile` dans le `pom.xml`.

## 10. Contact / Auteur
- **Développeur**: TAMO FOTSO Michel Darryl

---
Fichier généré automatiquement par assistant. Si tu veux des ajouts (diagrammes d'architecture, export d'API endpoints, ou une version en PDF), dis-moi laquelle et je la génère.