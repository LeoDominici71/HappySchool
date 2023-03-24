package com.HappySchool.Project.Controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

import com.HappySchool.Project.entities.dto.GradesDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class GradesControllerIT {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;

	private Integer existingId;
	private Integer nonExistingId;
	private Long existingIdStudent;
	private Long nonExistingIdStudent;

	@BeforeEach
	void setUp() throws Exception {
		existingId = 1;
		existingIdStudent = 2L;
		nonExistingId = 1000;
		nonExistingIdStudent = 1000L;

	}

	@Test
	public void DeleteShouldReturnNoContentWhenIdExists() throws Exception {

		ResultActions result = mockMvc
				.perform(delete("/grades/{id}/{id}", existingIdStudent, existingId ).accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isNoContent());
	}

	@Test
	public void DeleteShouldReturnNotFoundWhenCursoIdDoesNotExists() throws Exception {
		ResultActions result = mockMvc.perform(delete("/grades/{id}/{id}", nonExistingIdStudent, existingId)
				.accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isNotFound());
	}
	
	@Test
	public void DeleteShouldReturnNotFoundWhenStudentIdDoesNotExists() throws Exception {
		ResultActions result = mockMvc.perform(delete("/grades/{id}/{id}", existingIdStudent, nonExistingId)
				.accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isNotFound());
	}

	@Test
	public void findAllShouldReturnGrades() throws Exception {
		// When
		ResultActions result = mockMvc.perform(get("/grades").accept(MediaType.APPLICATION_JSON));

		// then
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("[0].grades").value(9.0));
		result.andExpect(jsonPath("[1].grades").value(9.0));

	}

	@Test
	public void InsertShouldCreateCurso() throws Exception {
		// given
		GradesDTO newGrades = new GradesDTO(2L, 2, 9.0);
		// when
		String jsonBody = objectMapper.writeValueAsString(newGrades);
		ResultActions result = mockMvc.perform(post("/grades").content(jsonBody).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));

		// then
		result.andExpect(status().isCreated());
		result.andExpect(jsonPath("$.grades").exists());
	}

}
