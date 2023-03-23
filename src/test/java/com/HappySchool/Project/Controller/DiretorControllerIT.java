package com.HappySchool.Project.Controller;

import com.HappySchool.Project.entities.Diretor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class DiretorControllerIT {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;

	private Integer existingId;
	private Integer nonExistingId;
	private Diretor NewDiretorId1ToUpdate;

	@BeforeEach
	void setUp() throws Exception {
		existingId = 1;
		nonExistingId = 1000;

	}

	@Test
	public void DeleteShouldReturnNoContentWhenIdExists() throws Exception {


		ResultActions result = mockMvc.perform(delete("/Diretors/{id}", existingId).accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isNoContent());
	}

	@Test
	public void DeleteShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
		ResultActions result = mockMvc
				.perform(delete("/Diretors/{id}", nonExistingId).accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isNotFound());
	}

	@Test
	public void findAllShouldReturnDiretors() throws Exception {
		// When
		ResultActions result = mockMvc.perform(get("/Diretors").accept(MediaType.APPLICATION_JSON));

		// then
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("[0].nome").value("Carlos"));
		result.andExpect(jsonPath("[0].cpf").value("75571745002"));
		result.andExpect(jsonPath("[1].nome").value("Isak"));
		result.andExpect(jsonPath("[1].cpf").value("01383347069"));

	}

	@Test
	public void findByIdShouldReturnDiretorWhenIdExists() throws Exception {

		NewDiretorId1ToUpdate = new Diretor(1, "Carlos", "75571745002" );


		ResultActions result = mockMvc.perform(get("/Diretors/{id}", existingId).accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.matricula").value(existingId));
		result.andExpect(jsonPath("$.nome").value("Carlos"));
		result.andExpect(jsonPath("$.cpf").value("75571745002"));

	}

	@Test
	public void findByIdShouldReturnEntityNotFoundExceptionsWhenIdExists() throws Exception {
		ResultActions result = mockMvc.perform(get("/Diretors/{id}", nonExistingId).accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isNotFound());

	}

	@Test
	public void InsertShouldCreateDiretor() throws Exception {
		// given
		Diretor newDiretorWithId1 = new Diretor(1, "Carlos", "25331095097");
		// when
		String jsonBody = objectMapper.writeValueAsString(newDiretorWithId1);
		ResultActions result = mockMvc.perform(post("/Diretors").content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

		// then
		result.andExpect(status().isCreated());
		result.andExpect(jsonPath("$.matricula").exists());
		result.andExpect(jsonPath("$.nome").exists());
		result.andExpect(jsonPath("$.cpf").exists());
	}

	@Test
	public void updateShouldReturnDiretorWhenIdExists() throws Exception {
		// given
		NewDiretorId1ToUpdate = new Diretor(1, "Carlos", "25331095097" );

		String expectedName = NewDiretorId1ToUpdate.getNome();

		// when
		String jsonBody = objectMapper.writeValueAsString(NewDiretorId1ToUpdate);
		ResultActions result = mockMvc.perform(put("/Diretors/{id}", existingId).content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

		// then
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.matricula").value(existingId));
		result.andExpect(jsonPath("$.nome").value(expectedName));
		result.andExpect(jsonPath("$.cpf").exists());
	}

	@Test
	public void updateShouldReturnDiretorNotFoundWhenIdDoesNotExists() throws Exception {
		// given
		NewDiretorId1ToUpdate = new Diretor(1, "Carlos", "25331095097" );
		String jsonBody = objectMapper.writeValueAsString(NewDiretorId1ToUpdate);

		// when
		ResultActions result = mockMvc.perform(put("/Diretors/{id}", nonExistingId).content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

		// then
		result.andExpect(status().isNotFound());

	}

}
