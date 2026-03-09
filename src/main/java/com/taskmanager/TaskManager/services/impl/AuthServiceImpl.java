package com.taskmanager.TaskManager.services.impl;

import com.taskmanager.TaskManager.dtos.request.AuthRequest;
import com.taskmanager.TaskManager.dtos.request.InscriptionRequest;
import com.taskmanager.TaskManager.dtos.response.AuthResponse;
import com.taskmanager.TaskManager.dtos.response.UtilisateurResponse;
import com.taskmanager.TaskManager.entities.Utilisateur;
import com.taskmanager.TaskManager.exception.EmailDejaUtiliseException;
import com.taskmanager.TaskManager.mappers.UtilisateurMapper;
import com.taskmanager.TaskManager.repositories.UtilisateurRepository;
import com.taskmanager.TaskManager.services.IAuthService;
import com.taskmanager.TaskManager.config.JwtService;
import com.taskmanager.TaskManager.utils.RoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {

    private final UtilisateurRepository utilisateurRepository;
    private final UtilisateurMapper utilisateurMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public UtilisateurResponse inscrire(InscriptionRequest request) {
        if (utilisateurRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailDejaUtiliseException("Cet email est déjà utilisé !");
        }

        Utilisateur utilisateur = Utilisateur.builder()
                .nom(request.getNom())
                .prenom(request.getPrenom())
                .email(request.getEmail())
                .motDePasse(passwordEncoder.encode(request.getMotDePasse()))
                .role(RoleEnum.USER)
                .statut("ACTIF")
                .build();

        Utilisateur savedUtilisateur = utilisateurRepository.save(utilisateur);
        return utilisateurMapper.toResponse(savedUtilisateur);
    }

    @Override
    public AuthResponse authentifier(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getMotDePasse()
                )
        );

        Utilisateur utilisateur = utilisateurRepository.findByEmail(request.getEmail())
                .orElseThrow();
        
        String jwtToken = jwtService.generateToken(utilisateur);
        
        return new AuthResponse(jwtToken, utilisateurMapper.toResponse(utilisateur));
    }
}
