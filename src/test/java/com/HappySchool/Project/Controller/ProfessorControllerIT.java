package com.HappySchool.Project.Controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.HappySchool.Project.entities.Professor;
import com.HappySchool.Project.tests.Factory;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProfessorControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private Long existingId;
    private Long nonExistingId;
    private Long countTotalProfessors;
    private Professor NewProfessorId1ToUpdate;
    private Professor newProfessorWithId4;
    private Professor SameCpfProfessor;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 1000L;
        countTotalProfessors = 3L;

    }

    @Test
    public void DeleteShouldReturnNoContentWhenIdExists() throws Exception {
        ResultActions result = mockMvc
                .perform(delete("/professors/{id}", existingId).accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isNoContent());
    }

    @Test
    public void DeleteShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
        ResultActions result = mockMvc
                .perform(delete("/professors/{id}", nonExistingId).accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isNotFound());
    }


    @Test
    public void findByIdShouldReturnProfessorWhenIdExists() throws Exception {

        ResultActions result = mockMvc.perform(get("/professors/{id}", existingId).accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.matricula").value(existingId));
        result.andExpect(jsonPath("$.nome").value("Marcos"));
        result.andExpect(jsonPath("$.cpf").value("48374255854"));
        result.andExpect(jsonPath("$.especialidade").value("Java"));

    }

    @Test
    public void findByIdShouldReturnEntityNotFoundExceptionsWhenIdExists() throws Exception {
        ResultActions result = mockMvc
                .perform(get("/professors/{id}", nonExistingId).accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isNotFound());

    }

    @Test
    public void InsertShouldCreateProfessor() throws Exception {
        // given
        newProfessorWithId4 = Factory.createNewProfessor();
        // when
        String jsonBody = objectMapper.writeValueAsString(newProfessorWithId4);
        ResultActions result = mockMvc.perform(post("/professors").content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

        // then
        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.matricula").exists());
        result.andExpect(jsonPath("$.nome").exists());
        result.andExpect(jsonPath("$.cpf").exists());
        result.andExpect(jsonPath("$.especialidade").exists());

    }


    @Test
    public void updateShouldReturnProfessorWhenIdExists() throws Exception {
        // given
        NewProfessorId1ToUpdate = Factory.createProfessor();

        String expectedName = NewProfessorId1ToUpdate.getNome();

        // when
        String jsonBody = objectMapper.writeValueAsString(NewProfessorId1ToUpdate);
        ResultActions result = mockMvc.perform(put("/professors/{id}", existingId).content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

        // then
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.matricula").value(existingId));
        result.andExpect(jsonPath("$.nome").value(expectedName));
        result.andExpect(jsonPath("$.cpf").exists());
        result.andExpect(jsonPath("$.especialidade").exists());

    }

    @Test
    public void updateShouldReturnProfessorNotFoundWhenIdDoesNotExists() throws Exception {
        // given
        NewProfessorId1ToUpdate = Factory.createProfessor();
        String jsonBody = objectMapper.writeValueAsString(NewProfessorId1ToUpdate);

        // when
        ResultActions result = mockMvc.perform(put("/professors/{id}", nonExistingId).content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

        // then
        result.andExpect(status().isNotFound());

    }

}
