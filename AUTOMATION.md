# Automatisation du Déploiement Local 🚀

Ce document explique comment votre application se met à jour toute seule sur votre machine locale (Docker Desktop).

## Principe de fonctionnement
1. **GitHub Actions** : Compile votre code, crée une image Docker et l'envoie sur **Docker Hub** automatiquement à chaque push sur `main`.
2. **Docker Hub** : Héberge la dernière version de votre image (`darryl1234/taskmanager:latest`).
3. **Watchtower** (Local) : Tourne sur votre machine et vérifie toutes les 60 secondes si une nouvelle version existe sur Docker Hub.
4. **Mise à jour Auto** : Si une nouvelle image est trouvée, Watchtower l'arrête, la télécharge (pull) et redémarre l'application avec la nouvelle version.

## Installation (Une seule fois)

1. Assurez-vous que **Docker Desktop** est lancé.
2. Ouvrez un terminal dans le dossier du projet.
3. Lancez l'infrastructure :
   ```bash
   docker-compose up -d
   ```

## Vérification

Pour voir Watchtower en action, vous pouvez consulter ses logs :
```bash
docker logs -f watchtower
```

Vous verrez des messages indiquant qu'il vérifie les mises à jour pour le conteneur `taskmanager-app`.

## Notes Importantes
- **Image Name** : Si vous changez de nom d'utilisateur Docker Hub, n'oubliez pas de mettre à jour le champ `image:` dans le fichier `docker-compose.yml`.
- **Application** : L'application est maintenant accessible sur le port **8081** (au lieu de 8080).
- **Base de données** : Le port MySQL sur votre machine est maintenant **3307** (au lieu de 3306) pour éviter les conflits si vous avez déjà un MySQL installé localement.
- **Secrets** : Les identifiants de base de données sont actuellement en clair dans le `docker-compose.yml`. Pour une production réelle, utilisez un fichier `.env`.
