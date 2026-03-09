package com.taskmanager.TaskManager.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskmanager.TaskManager.config.JwtService;
import com.taskmanager.TaskManager.dtos.request.TacheRequest;
import com.taskmanager.TaskManager.dtos.response.TacheResponse;
import com.taskmanager.TaskManager.repositories.UtilisateurRepository;
import com.taskmanager.TaskManager.services.ITacheService;
import com.taskmanager.TaskManager.utils.StatutTacheEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TacheController.class)
public class TacheControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ITacheService tacheService;

    @MockitoBean
    private UtilisateurRepository utilisateurRepository;

    @MockitoBean
    private JwtService jwtService;

    @Test
    public void testCreerTache_Success() throws Exception {
        TacheRequest request = new TacheRequest();
        request.setTitre("Ma Nouvelle Tâche");
        request.setStatut(StatutTacheEnum.A_FAIRE);

        TacheResponse response = new TacheResponse();
        response.setTitre("Ma Nouvelle Tâche");
        response.setStatut(StatutTacheEnum.A_FAIRE);

        when(tacheService.creer(any(), any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/taches")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.titre").value("Ma Nouvelle Tâche"));
    }

    @Test
    public void testObtenirToutesLesTaches_Success() throws Exception {
        TacheResponse tache1 = new TacheResponse();
        tache1.setTitre("Tâche 1");

        Page<TacheResponse> page = new PageImpl<>(List.of(tache1));

        when(tacheService.obtenirToutes(anyInt(), anyInt(), anyString())).thenReturn(page);

        mockMvc.perform(get("/api/v1/taches")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content[0].titre").value("Tâche 1"));
    }
}
