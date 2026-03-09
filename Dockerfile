# ÉTAPE 1 : Construction (Build)
FROM maven:3.9.4-eclipse-temurin-17 AS build

# Définition du répertoire de travail dans le conteneur de build
WORKDIR /workspace

# Copie du fichier de configuration Maven (pom.xml) pour installer les dépendances
COPY pom.xml ./

# Copie du dossier source (code de l'application)
COPY src ./src

# Compilation et création du fichier JAR
# L'option -DskipTests ignore les tests ici car le pipeline CI les exécute déjà avant
RUN mvn -B -DskipTests package

FROM eclipse-temurin:17-jre-jammy

# Définition du répertoire de travail final
WORKDIR /app
COPY --from=build /workspace/target/TaskManager-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
