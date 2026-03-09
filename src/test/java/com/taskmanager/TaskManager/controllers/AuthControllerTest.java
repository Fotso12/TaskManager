package com.taskmanager.TaskManager.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskmanager.TaskManager.dtos.request.AuthRequest;
import com.taskmanager.TaskManager.dtos.request.InscriptionRequest;
import com.taskmanager.TaskManager.dtos.response.AuthResponse;
import com.taskmanager.TaskManager.dtos.response.UtilisateurResponse;
import com.taskmanager.TaskManager.services.IAuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private IAuthService authService;

    @Test
    public void testInscription_Success() throws Exception {
        InscriptionRequest request = new InscriptionRequest();
        request.setNom("Doe");
        request.setPrenom("John");
        request.setEmail("john.doe@example.com");
        request.setMotDePasse("password123");

        UtilisateurResponse response = new UtilisateurResponse();
        response.setEmail("john.doe@example.com");

        when(authService.inscrire(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/auth/inscription")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.email").value("john.doe@example.com"));
    }

    @Test
    public void testConnexion_Success() throws Exception {
        AuthRequest request = new AuthRequest();
        request.setEmail("john.doe@example.com");
        request.setMotDePasse("password123");

        AuthResponse response = new AuthResponse("mock-jwt-token", new UtilisateurResponse());

        when(authService.authentifier(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/auth/connexion")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.token").value("mock-jwt-token"));
    }
}
