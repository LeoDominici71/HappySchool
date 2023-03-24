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

import com.HappySchool.Project.entities.Curso;
import com.HappySchool.Project.entities.Professor;
import com.HappySchool.Project.entities.dto.CursoDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class CurseControllerIT {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;

	private Integer existingId;
	private Integer nonExistingId;

	@BeforeEach
	void setUp() throws Exception {
		existingId = 1;
		nonExistingId = 1000;

	}

	@Test
	public void DeleteShouldReturnNoContentWhenIdExists() throws Exception {

		ResultActions result = mockMvc.perform(delete("/cursos/{id}", existingId).accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isNoContent());
	}

	@Test
	public void DeleteShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
		ResultActions result = mockMvc
				.perform(delete("/cursos/{id}", nonExistingId).accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isNotFound());
	}

	@Test
	public void findAllShouldReturnCursos() throws Exception {
		// When
		ResultActions result = mockMvc.perform(get("/cursos").accept(MediaType.APPLICATION_JSON));

		// then
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("[0].nome").value("Java"));
		result.andExpect(jsonPath("[0].descricao").value("Java com Spring"));
		result.andExpect(jsonPath("[1].nome").value("Python"));
		result.andExpect(jsonPath("[1].descricao").value("Python com Jupyter"));

	}

	@Test
	public void findByIdShouldReturnCursoWhenIdExists() throws Exception {

		ResultActions result = mockMvc.perform(get("/cursos/{id}", existingId).accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").value(existingId));
		result.andExpect(jsonPath("$.nome").value("Java"));
		result.andExpect(jsonPath("$.descricao").value("Java com Spring"));

	}

	@Test
	public void findByIdShouldReturnEntityNotFoundExceptionsWhenIdExists() throws Exception {
		ResultActions result = mockMvc.perform(get("/cursos/{id}", nonExistingId).accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isNotFound());

	}

	@Test
	public void InsertShouldCreateCurso() throws Exception {
		// given
		CursoDTO newCurso = new CursoDTO("Java", "Java com Struts", 1L);
		// when
		String jsonBody = objectMapper.writeValueAsString(newCurso);
		ResultActions result = mockMvc.perform(post("/cursos").content(jsonBody).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));

		// then
		result.andExpect(status().isCreated());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.nome").exists());
		result.andExpect(jsonPath("$.descricao").exists());
	}

	@Test
	public void updateShouldReturnCursoWhenIdExists() throws Exception {
		// given]
		Professor NewProfessorId1ToUpdate = new Professor(1L, "Marcos", "48374255854","Java" );
		Curso NewCursoId1ToUpdate = new Curso(1, "Java", "Java com Struts", NewProfessorId1ToUpdate);

		String expectedName = NewCursoId1ToUpdate.getNome();

		// when
		String jsonBody = objectMapper.writeValueAsString(NewCursoId1ToUpdate);
		ResultActions result = mockMvc.perform(put("/cursos/{id}", existingId).content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

		// then
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").value(existingId));
		result.andExpect(jsonPath("$.nome").value(expectedName));
		result.andExpect(jsonPath("$.descricao").exists());
	}

	@Test
	public void updateShouldReturnCursoNotFoundWhenIdDoesNotExists() throws Exception {
		// given
		Professor NewProfessorId1ToUpdate = new Professor(1L, "Marcos", "48374255854","Java" );
		Curso NewCursoId1ToUpdate = new Curso(1, "Java", "Java com Spring",NewProfessorId1ToUpdate);
		String jsonBody = objectMapper.writeValueAsString(NewCursoId1ToUpdate);

		// when
		ResultActions result = mockMvc.perform(put("/cursos/{id}", nonExistingId).content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

		// then
		result.andExpect(status().isNotFound());

	}

}
