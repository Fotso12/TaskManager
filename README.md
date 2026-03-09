# TaskManager – API REST Spring Boot

> **Projet d'examen** – Pipeline CI/CD complet avec Spring Boot, GitHub Actions, SonarCloud et Docker.

---

## QUESTION 1 – CONCEPTION ARCHITECTURALE ET MODÉLISATION

### 1.1 Reformulation du besoin

**Acteurs :**
- **Utilisateur authentifié** : crée, consulte, modifie et supprime ses propres tâches.
- **Administrateur** : accès global à toutes les tâches et utilisateurs.
- **Système** : gère la sécurité JWT, la validation des données et la pagination.

**Fonctionnalités :**
- Inscription et authentification par JWT.
- CRUD complet des tâches (titre, description, statut, priorité, date d'échéance).
- Pagination des listes de tâches.
- Gestion des rôles (USER / ADMIN).
- Validation stricte des entrées et gestion centralisée des exceptions.

**Contraintes :**
- API REST versionnée (`/api/v1/`).
- Stateless (JWT, pas de session serveur).
- Base de données MySQL (prod) / H2 (tests).
- Couverture de tests ≥ 60%.

---

### 1.2 Modélisation des données

```
┌─────────────────────────┐        ┌──────────────────────────────┐
│       utilisateur        │        │            tache             │
├─────────────────────────┤        ├──────────────────────────────┤
│ PK iduser (INT, AI)     │◄──┐    │ PK idtache (INT, AI)         │
│    nom (VARCHAR 50) NN  │   │    │    titre (VARCHAR 100) NN    │
│    prenom (VARCHAR 50)  │   └────│ FK iduser (INT) NN           │
│    email (VARCHAR 100)  │        │    statut (ENUM) NN          │
│      UNIQUE NOT NULL    │        │    priorite (ENUM)           │
│    motdepasse (VARCHAR) │        │    description (TEXT)        │
│    role (ENUM)          │        │    dateecheance (DATETIME)   │
│    statut (VARCHAR 20)  │        │    datecreation (DATETIME)   │
│    datecreation         │        │    datemodification          │
└─────────────────────────┘        └──────────────────────────────┘
```

**Relations :** Un utilisateur possède plusieurs tâches (1-N). Clé étrangère `iduser` dans `tache` avec contrainte NOT NULL.

**Enums :** `StatutTacheEnum` (A_FAIRE, EN_COURS, TERMINE) | `PrioriteTacheEnum` (BASSE, MOYENNE, HAUTE) | `RoleEnum` (USER, ADMIN)

---

### 1.3 Structure REST

| Méthode | Route | Description | Code réponse |
|---------|-------|-------------|--------------|
| POST | `/api/v1/auth/inscription` | Inscription | 201 Created |
| POST | `/api/v1/auth/connexion` | Authentification JWT | 200 OK |
| POST | `/api/v1/taches` | Créer une tâche | 201 Created |
| GET | `/api/v1/taches` | Lister (paginé) | 200 OK |
| GET | `/api/v1/taches/{id}` | Détail d'une tâche | 200 / 404 |
| PUT | `/api/v1/taches/{id}` | Modifier une tâche | 200 / 404 |
| DELETE | `/api/v1/taches/{id}` | Supprimer une tâche | 200 / 404 |
| GET | `/api/v1/utilisateurs` | Lister les utilisateurs | 200 OK |

**Gestion d'erreurs :** `GlobalExceptionHandler` retourne des réponses `ApiResponse<>` uniformes.  
**Versioning :** Préfixe `/api/v1/` dans tous les endpoints.

---

### 1.4 Architecture Spring Boot

```
src/
├── main/java/com/taskmanager/
│   ├── config/          ← Sécurité (JWT, Spring Security, CORS, Swagger)
│   ├── controllers/     ← Entrée HTTP (TacheController, AuthController, UtilisateurController)
│   ├── services/        ← Logique métier (interfaces + implémentations impl/)
│   ├── repositories/    ← Accès BD (Spring Data JPA)
│   ├── entities/        ← Entités JPA (Tache, Utilisateur)
│   ├── dtos/            ← Transfert de données (request/ + response/)
│   ├── mappers/         ← Conversion entité ↔ DTO
│   ├── exception/       ← Exceptions métier + GlobalExceptionHandler
│   └── utils/           ← Énumérations (StatutTache, Priorite, Role)
└── resources/
    └── application.properties
```

**Principes SOLID appliqués :**
- **S** : chaque classe a une responsabilité unique (Controller → HTTP, Service → logique, Repository → BD).
- **O** : interfaces `ITacheService`, `IAuthService` permettent l'extension sans modification.
- **D** : injection par constructeur (`@RequiredArgsConstructor`), pas d'implémentation directe.

---

## QUESTION 2 – RÉALISATION, QUALITÉ ET SÉCURITÉ

### 2.1 Implémentation

- **JWT** : `JwtService` génère/valide les tokens, `JwtAuthFilter` intercepte chaque requête.
- **CRUD tâches** : create, read (paginé), update, delete avec vérification de propriété (un user ne voit que ses tâches).
- **Validation** : annotations `@NotNull`, `@NotBlank`, `@Size` sur les DTOs avec `@Valid` dans les controllers.
- **Pagination** : `tacheService.obtenirToutes(page, size, username)` retourne un `Page<TacheResponse>`.
- **Exceptions** : `ResourceNotFoundException` (404), `EmailDejaUtiliseException` (409), `GlobalExceptionHandler` (500).

---

### 2.2 Tests automatisés

| Type | Classe | Couverture |
|------|--------|------------|
| Service | `TacheServiceTest`, `AuthServiceTest` | Logique métier mockée |
| Controller | `TacheControllerTest`, `AuthControllerTest` | `@WebMvcTest` + `@WithMockUser` |
| Repository | `TacheRepositoryTest` | `@DataJpaTest` + H2 |
| App | `TaskManagerApplicationTests` | Démarrage contexte complet |

**Technologies :** JUnit 5, Mockito, MockMvc, `@MockitoBean`, `@WithMockUser`, Spring Security Test.

---

### 2.3 Sécurité

- **JWT stateless** : aucune session serveur, token Bearer dans le header.
- **Validation stricte** : `@Valid` + `BindingResult`, types Java forts (enums plutôt que strings libres).
- **Protection injections** : Spring Data JPA avec requêtes paramétrées (pas de SQL natif).
- **Gestion des rôles** : `RoleEnum` (USER/ADMIN), `SecurityConfig` avec règles par rôle.
- **CSRF** : désactivé pour l'API REST stateless (JWT remplace la protection CSRF).
- **CORS** : configuré via `CorsConfig`.

---

### 2.4 Clean Code

- Méthodes courtes et focalisées (< 20 lignes en général).
- Nommage en français cohérent avec le domaine métier.
- DTOs distincts pour request/response (pas d'exposition d'entités JPA).
- `@RequiredArgsConstructor` pour l'injection (pas de `@Autowired` sur champs).
- `ApiResponse<T>` générique pour toutes les réponses HTTP uniformes.

---

## QUESTION 3 – CONCEPTION DU PIPELINE

### 3.1 Stratégie de branches

**GitFlow simplifié :**
```
main     ← branche stable (déploiement auto)
develop  ← intégration des features
feature/ ← développement des fonctionnalités
```

**Déclencheurs :**
- `push` sur `main` ou `develop` → pipeline complet.
- `pull_request` vers `main` ou `develop` → CI (build + tests + analyse).

---

### 3.2 Schéma textuel du pipeline

```
PUSH / PR
    │
    ▼
┌─────────────────────────────┐
│  1. BUILD & TEST             │  ← Job: build-and-test
│  • mvn clean package        │
│  • maven-checkstyle (lint)  │
│  • mvn test                 │
└──────────────┬──────────────┘
               │ (si succès)
       ┌───────┴────────┐
       ▼                ▼
┌────────────┐  ┌──────────────────┐
│ 2. SONAR   │  │ 3. SECURITY SCAN │
│ • mvn verify│  │ • Trivy (fs)     │
│ • sonar:sonar│ │ • Gitleaks       │
└─────┬──────┘  └────────┬─────────┘
      │                  │
      └────────┬──────────┘
               │ (si main uniquement)
               ▼
┌──────────────────────────────┐
│  4. DOCKER BUILD & PUSH      │
│  • docker build              │
│  • docker push (SHA + latest)│
└──────────────┬───────────────┘
               │
               ▼
┌──────────────────────────────┐
│  5. DEPLOY                   │
│  • SSH → docker-compose up   │
└──────────────────────────────┘
```

---

### 3.3 Rôle de chaque étape

| Étape | Rôle | Valeur |
|-------|------|--------|
| Build & Test | Vérifie la compilabilité et la non-régression | **Maintenabilité** |
| Lint | Uniformité du style de code | **Lisibilité** |
| SonarCloud | Détection de bugs, dette technique, couverture | **Qualité** |
| Trivy | Vulnérabilités dans les dépendances | **Sécurité** |
| Gitleaks | Secrets dans le code (tokens, mdp) | **Sécurité** |
| Docker build | Artéfact immuable et reproductible | **Traçabilité** |
| Deploy | Mise en production automatique | **Observabilité** |

---

## QUESTION 4 – IMPLÉMENTATION DU PIPELINE

### 4.1 Intégration Continue

Le job `build-and-test` exécute automatiquement :
1. `mvn clean package -DskipTests` → compilation
2. `maven-checkstyle` → lint (non bloquant)
3. `mvn test` → tests unitaires avec rapport JaCoCo préparé

### 4.2 Intégration SonarCloud

- **Méthode :** `mvn verify` génère le rapport JaCoCo, puis `mvn sonar:sonar` envoie l'analyse.
- **Quality Gate :** configurée sur SonarCloud (Security A, Reliability A, Maintainability A, Coverage ≥ 60%).
- **Configuration :** `sonar-project.properties` + SONAR_TOKEN en secret GitHub.

```properties
sonar.projectKey=Fotso12_TaskManager
sonar.organization=fotso12
sonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml
```

### 4.3 Scan de sécurité (DevSecOps)

| Outil | Cible | Vulnérabilités détectées |
|-------|-------|--------------------------|
| **Trivy** | Système de fichiers du projet | Dépendances avec CVEs connus |
| **Gitleaks** | Historique Git | Secrets, tokens, mots de passe |

**Corrections appliquées :**
- Aucun secret en dur dans le code (utilisation des secrets GitHub Actions).
- Dépendances maintenues à jour (Spring Boot 3.4.3, jjwt 0.11.5).
- MySQL password vide en développement uniquement (variable d'environnement en prod).

---

## QUESTION 5 – DÉPLOIEMENT AUTOMATIQUE

### Architecture de déploiement

```yaml
# docker-compose.yml sur le serveur
services:
  app:
    image: dockerhub_user/taskmanager:latest
    ports:
      - "8082:8082"
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://db:3306/taskmanager
    restart: always   # Redémarrage automatique
  db:
    image: mysql:8
    restart: always
```

### Processus automatisé

1. **Build** : `docker build -t user/taskmanager:${SHA} .`
2. **Tag** : SHA du commit + `latest`
3. **Push** : Docker Hub
4. **Deploy** : SSH → `docker-compose pull app && docker-compose up -d`
5. **Restart** : `restart: always` assure le redémarrage automatique

---

## QUESTION 6 – OPTIMISATION & CLEAN CODE

### 6.1 Structuration et modularité

- **DTOs séparés** (`request/` + `response/`) : le domaine interne n'est jamais exposé directement.
- **Interfaces de service** (`ITacheService`, `IAuthService`) : découplage et testabilité.
- **Mappers** : conversion centralisée entre entités et DTOs.
- **Exception handler global** : un point unique de gestion des erreurs HTTP.

### 6.2 Optimisation du pipeline

**Parallélisation :** Les jobs `sonarcloud` et `security-scan` s'exécutent en parallèle après `build-and-test`.

**Mise en cache (à ajouter) :**
```yaml
- name: Cache Maven
  uses: actions/cache@v4
  with:
    path: ~/.m2/repository
    key: ${{ runner.os }}-maven-${{ hashFiles('**/pom.xml') }}
```

**Optimisations appliquées :**
- `mvn -B` (batch mode) : désactive la barre de progression, plus rapide en CI.
- `DskipTests` sur le build initial, tests séparés pour meilleur parallélisme.
- Jobs `deploy` et `docker-build-push` conditionnés à `main` uniquement.

### 6.3 Guide d'utilisation

#### Exécuter le pipeline
```bash
git push origin main        # Déclenche le pipeline complet
git push origin develop     # Déclenche CI uniquement (pas de déploiement)
```

#### Corriger un échec de pipeline
1. Aller sur GitHub → **Actions** → cliquer sur le run échoué.
2. Ouvrir le job en erreur → lire les logs.
3. Corriger localement → `git commit` → `git push`.

#### Interpréter les rapports SonarCloud
- **Security** : vulnérabilités dans le code → corriger en priorité.
- **Reliability** : bugs potentiels → revoir la logique.
- **Maintainability** : dette technique (code smells) → refactorer.
- **Coverage** : % de code testé → ajouter des tests si < 60%.
- **Duplications** : code dupliqué → extraire en méthodes réutilisables.

#### Corriger une vulnérabilité Trivy/Gitleaks
- **Trivy** : mettre à jour la dépendance vulnérable dans `pom.xml`.
- **Gitleaks** : supprimer le secret du code, le mettre dans les secrets GitHub, et purger l'historique Git (`git filter-branch` ou BFG).

---

## Prérequis et secrets GitHub

| Secret | Description |
|--------|-------------|
| `SONAR_TOKEN` | Token d'accès SonarCloud |
| `SONAR_ORGANIZATION` | Slug de l'organisation SonarCloud (`fotso12`) |
| `DOCKER_NAME` | Nom d'utilisateur Docker Hub |
| `DOCKER_TOKEN` | Token d'accès Docker Hub |
| `SSH_HOST` | Adresse IP du serveur de déploiement |
| `SSH_USER` | Utilisateur SSH |
| `SSH_PRIVATE_KEY` | Clé privée SSH |
| `SSH_PORT` | Port SSH (défaut : 22) |

## Documentation API

- **Swagger UI** : `http://localhost:8082/swagger-ui.html`
- **OpenAPI JSON** : `http://localhost:8082/v3/api-docs`
