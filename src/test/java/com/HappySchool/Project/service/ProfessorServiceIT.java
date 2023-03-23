package com.HappySchool.Project.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.HappySchool.Project.entities.Professor;
import com.HappySchool.Project.repository.ProfessorRepository;
import com.HappySchool.Project.services.ProfessorService;
import com.HappySchool.Project.servicesException.DatabaseExceptions;
import com.HappySchool.Project.servicesException.EntityNotFoundExceptions;
import com.HappySchool.Project.servicesException.RegistrationExceptions;
import com.HappySchool.Project.tests.Factory;

@SpringBootTest
@Transactional
public class ProfessorServiceIT {

	@Autowired
	private ProfessorService service;

	@Autowired
	private ProfessorRepository repository;

	private Long existingId;
	private Long nonExistingId;
	private Long countTotalProfessors;
	private Professor NewProfessorId3ToUpdate;
	private Professor NewProfessorId1ToUpdate;
	private Professor LastProfessorId3ToUpdate;
	private Professor newProfessorWithId4;
	private Professor SameCpfProfessor;

	@BeforeEach
	void setUp() throws Exception {
		existingId = 3L;
		nonExistingId = 1000L;
		countTotalProfessors = 3L;

	}

	@Test
	@DisplayName("Delete should throw EntityNotFound")
	public void deleteShouldThrowEntityNotFoundWhenIdDoesNotExists() {
		assertThrows(EntityNotFoundExceptions.class, () -> {
			service.delete(nonExistingId);
		});
	}

	@Test
	@DisplayName("Should return a list of Professors")
	public void testFindAll() {

		// when
		List<Professor> Professors = service.findAll();
		// then
		assertNotNull(Professors);
		assertEquals(repository.count(), Professors.size());
	}

	@Test
	@DisplayName("It should return a Professor by Id")
	public void findByIdShouldReturnProfessorWhenIdExist() {
		// when
		Professor result = service.findById(existingId);
		// then
		assertNotNull(result);
		assertEquals(repository.findById(existingId).get().getCpf(), result.getCpf());
	}

	@Test
	@DisplayName("It should thrown EntityNotFoundException")
	public void findByIdShouldReturnProfessorWhenIdDoesNotExist() {
		assertThrows(EntityNotFoundExceptions.class, () -> {
			service.findById(nonExistingId);
		});
	}

	@Test
	@DisplayName("Save should save a Professor")
	public void InsertShouldReturnProfessorWhenIdExists() {
		// given
		newProfessorWithId4 = Factory.createNewProfessor();

		// When
		Professor insertedProfessor = service.insert(newProfessorWithId4);
		// then
		assertNotNull(newProfessorWithId4);
		assertEquals(newProfessorWithId4, insertedProfessor);
		assertEquals(newProfessorWithId4.getNome(), insertedProfessor.getNome());
		assertEquals(newProfessorWithId4.getCpf(), insertedProfessor.getCpf());
	}

	
	@Test
	@DisplayName("Update should update a Professor")
	public void UpdateShouldReturnProfessorWhenIdExist() {
		// given
		LastProfessorId3ToUpdate = Factory.createProfessorToUpdate();
		NewProfessorId3ToUpdate = Factory.createProfessorId3();

		// when
		Professor UpdatedProfessorId3 = service.update(existingId, NewProfessorId3ToUpdate);

		// then
		assertNotNull(UpdatedProfessorId3);
		assertEquals(NewProfessorId3ToUpdate, UpdatedProfessorId3);
		assertEquals(NewProfessorId3ToUpdate.getNome(), UpdatedProfessorId3.getNome());
		assertNotEquals(LastProfessorId3ToUpdate.getNome(), UpdatedProfessorId3.getNome());

	}

	@Test
	@DisplayName("update an inexistent id should display an EntityNotFoundException")
	public void UpdateShouldReturnProfessorWhenIdDoesNotExist() {

		NewProfessorId1ToUpdate = Factory.createNewProfessor();

		assertThrows(EntityNotFoundExceptions.class, () -> {
			service.update(nonExistingId, NewProfessorId1ToUpdate);
		});

	}

}
