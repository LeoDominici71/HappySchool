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

import com.HappySchool.Project.entities.Student;
import com.HappySchool.Project.tests.Factory;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class StudentControllerIT {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;

	private Long existingId;
	private Long nonExistingId;
	private Long countTotalStudents;
	private Student NewstudentId1ToUpdate;
	private Student newstudentWithId5;
	private Student SameCpfStudent;

	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 1000L;
		countTotalStudents = 4L;

	}

	@Test
	public void DeleteShouldReturnNoContentWhenIdExists() throws Exception {
		ResultActions result = mockMvc.perform(delete("/students/{id}", existingId).accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isNoContent());
	}

	@Test
	public void DeleteShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
		ResultActions result = mockMvc
				.perform(delete("/students/{id}", nonExistingId).accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isNotFound());
	}

	@Test
	public void findAllShouldReturnStudents() throws Exception {
		// When
		ResultActions result = mockMvc.perform(get("/students").accept(MediaType.APPLICATION_JSON));

		// then
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("[0].nome").value("Maria Brown"));
		result.andExpect(jsonPath("[0].cpf").value("48374255854"));
		result.andExpect(jsonPath("[1].nome").value("Alex Green"));
		result.andExpect(jsonPath("[1].cpf").value("70409951820"));

	}

	@Test
	public void findByIdShouldReturnStudentWhenIdExists() throws Exception {

		ResultActions result = mockMvc.perform(get("/students/{id}", existingId).accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.matricula").value(existingId));
		result.andExpect(jsonPath("$.nome").value("Maria Brown"));
		result.andExpect(jsonPath("$.cpf").value("48374255854"));

	}

	@Test
	public void findByIdShouldReturnEntityNotFoundExceptionsWhenIdExists() throws Exception {
		ResultActions result = mockMvc.perform(get("/students/{id}", nonExistingId).accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isNotFound());

	}

	@Test
	public void InsertShouldCreateStudent() throws Exception {
		// given
		newstudentWithId5 = Factory.CreateStudent5();
		// when
		String jsonBody = objectMapper.writeValueAsString(newstudentWithId5);
		ResultActions result = mockMvc.perform(post("/students").content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

		// then
		result.andExpect(status().isCreated());
		result.andExpect(jsonPath("$.matricula").exists());
		result.andExpect(jsonPath("$.nome").exists());
		result.andExpect(jsonPath("$.cpf").exists());
	}

	//@Test
	//public void InsertWhenCpfAlreadyExistCreateStudent() throws Exception {
		// given
		//SameCpfStudent = Factory.SameCpfStudent();
		// when
		//String jsonBody = objectMapper.writeValueAsString(SameCpfStudent);
		//ResultActions result = mockMvc.perform(post("/students").content(jsonBody)
				//.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

		// then
		//result.andExpect(status().isBadRequest());
	//}

	@Test
	public void updateShouldReturnStudentWhenIdExists() throws Exception {
		// given
		NewstudentId1ToUpdate = Factory.createStudent();

		String expectedName = NewstudentId1ToUpdate.getNome();

		// when
		String jsonBody = objectMapper.writeValueAsString(NewstudentId1ToUpdate);
		ResultActions result = mockMvc.perform(put("/students/{id}", existingId).content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

		// then
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.matricula").value(existingId));
		result.andExpect(jsonPath("$.nome").value(expectedName));
		result.andExpect(jsonPath("$.cpf").exists());
	}

	@Test
	public void updateShouldReturnStudentNotFoundWhenIdDoesNotExists() throws Exception {
		// given
		NewstudentId1ToUpdate = Factory.createStudent();
		String jsonBody = objectMapper.writeValueAsString(NewstudentId1ToUpdate);

		// when
		ResultActions result = mockMvc.perform(put("/students/{id}", nonExistingId).content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

		// then
		result.andExpect(status().isNotFound());

	}

}
