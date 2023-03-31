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
import com.HappySchool.Project.entities.Student;
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

	private Integer existingCourseId;
	private Integer nonExistingId;
	private Long existingStudentId;
	private Long nonExistingIdStudent;

	@BeforeEach
	void setUp() throws Exception {
		existingCourseId = 1;
		existingStudentId = 2L;
		nonExistingId = 1000;
		nonExistingIdStudent = 1000L;

	}

	@Test
	public void DeleteShouldReturnNoContentWhenIdExists() throws Exception {

		ResultActions result = mockMvc
				.perform(delete("/grades/{id}/{id}", existingStudentId, existingCourseId ).accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isNoContent());
	}

	@Test
	public void DeleteShouldReturnNotFoundWhenCursoIdDoesNotExists() throws Exception {
		ResultActions result = mockMvc.perform(delete("/grades/{id}/{id}", nonExistingIdStudent, existingCourseId)
				.accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isNotFound());
	}
	
	@Test
	public void DeleteShouldReturnNotFoundWhenStudentIdDoesNotExists() throws Exception {
		ResultActions result = mockMvc.perform(delete("/grades/{id}/{id}", existingStudentId, nonExistingId)
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
	
	@Test
	public void UpdateShouldReturnGradesWhenExist() throws Exception {
		// given
		GradesDTO newGrades = new GradesDTO(2L, 1, 8.0);
		// when
		String jsonBody = objectMapper.writeValueAsString(newGrades);
		ResultActions result = mockMvc.perform(put("/grades/{id}/{id}", 2L, 1).content(jsonBody).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));

		// then
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.grades").value(8.0));
	}
	
	@Test
	public void shouldReturnNotFoundWhenCourseIdDoesNotExistDuringGradeUpdate() throws Exception {
	    // setup
	    Curso nonExistentCourse = new Curso();
	    nonExistentCourse.setId(999);

	    GradesDTO newGrades = new GradesDTO(2L, nonExistentCourse.getId(), 8.0);

	    // execute
	    ResultActions result = mockMvc.perform(put("/grades/{studentId}/{courseId}", newGrades.getStudentId(), newGrades.getCourseId())
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(objectMapper.writeValueAsString(newGrades)));

	    // verify
	    result.andExpect(status().isNotFound());
	}
	@Test
	public void shouldReturnNotFoundWhenStudentIdDoesNotExistDuringGradeUpdate() throws Exception {
	    // setup
	    Student nonExistentStudent = new Student();
	    nonExistentStudent.setMatricula(999L);

	    GradesDTO newGrades = new GradesDTO(nonExistentStudent.getMatricula(),1 , 8.0);

	    // execute
	    ResultActions result = mockMvc.perform(put("/grades/{studentId}/{courseId}", newGrades.getStudentId(), newGrades.getCourseId())
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(objectMapper.writeValueAsString(newGrades)));

	    // verify
	    result.andExpect(status().isNotFound());
	}

}
