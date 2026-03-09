package com.taskmanager.TaskManager.repositories;

import com.taskmanager.TaskManager.entities.Tache;
import com.taskmanager.TaskManager.entities.Utilisateur;
import com.taskmanager.TaskManager.utils.RoleEnum;
import com.taskmanager.TaskManager.utils.StatutTacheEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class TacheRepositoryTest {

    @Autowired
    private TacheRepository tacheRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    private Utilisateur utilisateur;
    private Tache tache;

    @BeforeEach
    public void setup() {
        utilisateur = Utilisateur.builder()
                .nom("Doe")
                .prenom("John")
                .email("john.doe@example.com")
                .motDePasse("password123")
                .role(RoleEnum.USER)
                .statut("ACTIF")
                .build();
        utilisateur = utilisateurRepository.save(utilisateur);

        tache = Tache.builder()
                .titre("Test Tache")
                .description("Description Test")
                .statut(StatutTacheEnum.A_FAIRE)
                .dateEcheance(LocalDateTime.now().plusDays(2))
                .utilisateur(utilisateur)
                .build();
        tache = tacheRepository.save(tache);
    }

    @Test
    public void testFindByUtilisateurIdUser() {
        Page<Tache> tachesPage = tacheRepository.findByUtilisateurIdUser(utilisateur.getIdUser(), PageRequest.of(0, 10));

        assertThat(tachesPage.getContent()).hasSize(1);
        assertThat(tachesPage.getContent().get(0).getTitre()).isEqualTo("Test Tache");
    }

    @Test
    public void testFindByIdTacheAndUtilisateurIdUser() {
        Optional<Tache> foundTache = tacheRepository.findByIdTacheAndUtilisateurIdUser(tache.getIdTache(), utilisateur.getIdUser());

        assertThat(foundTache).isPresent();
        assertThat(foundTache.get().getTitre()).isEqualTo("Test Tache");
    }

    @Test
    public void testFindByIdTacheAndUtilisateurIdUser_NotFound() {
        Optional<Tache> foundTache = tacheRepository.findByIdTacheAndUtilisateurIdUser(tache.getIdTache(), utilisateur.getIdUser() + 999);

        assertThat(foundTache).isNotPresent();
    }
}
