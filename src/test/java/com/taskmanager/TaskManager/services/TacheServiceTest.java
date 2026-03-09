package com.taskmanager.TaskManager.services;

import com.taskmanager.TaskManager.dtos.request.TacheRequest;
import com.taskmanager.TaskManager.dtos.response.TacheResponse;
import com.taskmanager.TaskManager.entities.Tache;
import com.taskmanager.TaskManager.entities.Utilisateur;
import com.taskmanager.TaskManager.exception.ResourceNotFoundException;
import com.taskmanager.TaskManager.mappers.TacheMapper;
import com.taskmanager.TaskManager.repositories.TacheRepository;
import com.taskmanager.TaskManager.repositories.UtilisateurRepository;
import com.taskmanager.TaskManager.services.impl.TacheServiceImpl;
import com.taskmanager.TaskManager.utils.StatutTacheEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TacheServiceTest {

    @Mock
    private TacheRepository tacheRepository;

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @Mock
    private TacheMapper tacheMapper;

    @InjectMocks
    private TacheServiceImpl tacheService;

    private Utilisateur utilisateur;
    private Tache tache;
    private TacheRequest tacheRequest;

    @BeforeEach
    public void setup() {
        utilisateur = Utilisateur.builder().idUser(1).email("test@example.com").build();
        tache = Tache.builder().idTache(1).titre("Titre").utilisateur(utilisateur).build();

        tacheRequest = new TacheRequest();
        tacheRequest.setTitre("Titre");
        tacheRequest.setStatut(StatutTacheEnum.A_FAIRE);
    }

    @Test
    public void testCreer_Success() {
        when(utilisateurRepository.findByEmail(anyString())).thenReturn(Optional.of(utilisateur));
        when(tacheMapper.toEntity(any())).thenReturn(tache);
        when(tacheRepository.save(any())).thenReturn(tache);
        
        TacheResponse responseMock = new TacheResponse();
        responseMock.setTitre("Titre");
        when(tacheMapper.toResponse(any())).thenReturn(responseMock);

        TacheResponse result = tacheService.creer(tacheRequest, "test@example.com");

        assertThat(result.getTitre()).isEqualTo("Titre");
        verify(tacheRepository, times(1)).save(any(Tache.class));
    }

    @Test
    public void testCreer_UserNotFound() {
        when(utilisateurRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            tacheService.creer(tacheRequest, "inconnu@example.com");
        });
    }

    @Test
    public void testObtenirToutes_Success() {
        when(utilisateurRepository.findByEmail(anyString())).thenReturn(Optional.of(utilisateur));
        Page<Tache> pagedTasks = new PageImpl<>(List.of(tache));
        when(tacheRepository.findByUtilisateurIdUser(eq(1), any(Pageable.class))).thenReturn(pagedTasks);
        when(tacheMapper.toResponse(any())).thenReturn(new TacheResponse());

        Page<TacheResponse> result = tacheService.obtenirToutes(0, 10, "test@example.com");

        assertThat(result.getContent()).hasSize(1);
    }
}
