package com.taskmanager.TaskManager.services;

import com.taskmanager.TaskManager.config.JwtService;
import com.taskmanager.TaskManager.dtos.request.AuthRequest;
import com.taskmanager.TaskManager.dtos.request.InscriptionRequest;
import com.taskmanager.TaskManager.dtos.response.AuthResponse;
import com.taskmanager.TaskManager.dtos.response.UtilisateurResponse;
import com.taskmanager.TaskManager.entities.Utilisateur;
import com.taskmanager.TaskManager.exception.EmailDejaUtiliseException;
import com.taskmanager.TaskManager.mappers.UtilisateurMapper;
import com.taskmanager.TaskManager.repositories.UtilisateurRepository;
import com.taskmanager.TaskManager.services.impl.AuthServiceImpl;
import com.taskmanager.TaskManager.utils.RoleEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @Mock
    private UtilisateurMapper utilisateurMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthServiceImpl authService;

    private Utilisateur utilisateur;
    private InscriptionRequest inscriptionRequest;

    @BeforeEach
    public void setup() {
        utilisateur = Utilisateur.builder()
                .idUser(1)
                .nom("Doe")
                .prenom("John")
                .email("john.doe@example.com")
                .motDePasse("encodedPassword")
                .role(RoleEnum.USER)
                .statut("ACTIF")
                .build();

        inscriptionRequest = new InscriptionRequest();
        inscriptionRequest.setNom("Doe");
        inscriptionRequest.setPrenom("John");
        inscriptionRequest.setEmail("john.doe@example.com");
        inscriptionRequest.setMotDePasse("password123");
    }

    @Test
    public void testInscrire_Success() {
        when(utilisateurRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(utilisateurRepository.save(any(Utilisateur.class))).thenReturn(utilisateur);
        
        UtilisateurResponse responseMock = new UtilisateurResponse();
        responseMock.setEmail("john.doe@example.com");
        when(utilisateurMapper.toResponse(any(Utilisateur.class))).thenReturn(responseMock);

        UtilisateurResponse response = authService.inscrire(inscriptionRequest);

        assertThat(response).isNotNull();
        assertThat(response.getEmail()).isEqualTo("john.doe@example.com");
        verify(utilisateurRepository, times(1)).save(any(Utilisateur.class));
    }

    @Test
    public void testInscrire_EmailDejaUtilise() {
        when(utilisateurRepository.findByEmail(anyString())).thenReturn(Optional.of(utilisateur));

        assertThrows(EmailDejaUtiliseException.class, () -> {
            authService.inscrire(inscriptionRequest);
        });

        verify(utilisateurRepository, never()).save(any(Utilisateur.class));
    }

    @Test
    public void testAuthentifier_Success() {
        AuthRequest loginRequest = new AuthRequest();
        loginRequest.setEmail("john.doe@example.com");
        loginRequest.setMotDePasse("password123");

        when(utilisateurRepository.findByEmail(anyString())).thenReturn(Optional.of(utilisateur));
        when(jwtService.generateToken(any(Utilisateur.class))).thenReturn("mock-jwt-token");
        when(utilisateurMapper.toResponse(any(Utilisateur.class))).thenReturn(new UtilisateurResponse());

        AuthResponse response = authService.authentifier(loginRequest);

        assertThat(response).isNotNull();
        assertThat(response.getToken()).isEqualTo("mock-jwt-token");
        verify(authenticationManager, times(1)).authenticate(any());
    }
}
