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

import com.HappySchool.Project.entities.Student;
import com.HappySchool.Project.repository.StudentRepository;
import com.HappySchool.Project.services.StudentService;
import com.HappySchool.Project.servicesException.EntityNotFoundExceptions;
import com.HappySchool.Project.servicesException.RegistrationExceptions;
import com.HappySchool.Project.tests.Factory;

@SpringBootTest
@Transactional
public class StudentServiceIT {

	@Autowired
	private StudentService service;

	@Autowired
	private StudentRepository repository;

	private Long existingId;
	private Long nonExistingId;
	private Long countTotalStudents;
	private Student NewstudentId1ToUpdate;
	private Student LaststudentId1ToUpdate;
	private Student newstudentWithId5;
	private Student studentId5;
	private Student SameCpfStudent;

	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 1000L;
		countTotalStudents = 4L;
		
	}

	@Test
	@DisplayName("Delete should not result anything")
	public void deleteShouldDeleteResourceWhenIdExists() {
		// when
		service.delete(existingId);
		// then
		assertEquals(countTotalStudents - 1, repository.count());
	}

	@Test
	@DisplayName("Delete should throw EntityNotFound")
	public void deleteShouldThrowEntityNotFoundWhenIdDoesNotExists() {
		assertThrows(EntityNotFoundExceptions.class, () -> {
			service.delete(nonExistingId);
		});
	}

	@Test
	@DisplayName("Should return a list of students")
	public void testFindAll() {

		// when
		List<Student> students = service.findAll();
		// then
		assertNotNull(students);
		assertEquals(countTotalStudents, students.size());
	}

	@Test
	@DisplayName("It should return a student by Id")
	public void findByIdShouldReturnStudentWhenIdExist() {
		// when
		Student result = service.findById(existingId);
		// then
		assertNotNull(result);
		assertEquals(repository.findById(existingId).get().getCpf(), result.getCpf());
	}

	@Test
	@DisplayName("It should thrown EntityNotFoundException")
	public void findByIdShouldReturnStudentWhenIdDoesNotExist() {
		assertThrows(EntityNotFoundExceptions.class, () -> {
			service.findById(nonExistingId);
		});
	}

	@Test
	@DisplayName("Save should save a student")
	public void InsertShouldReturnStudentWhenIdExists() {
		// given
		newstudentWithId5 = Factory.CreateStudent5();

		// When
		Student insertedStudent = service.insert(studentId5);
		// then
		assertNotNull(studentId5);
		assertEquals(studentId5, insertedStudent);
		assertEquals(studentId5.getNome(), insertedStudent.getNome());
		assertEquals(studentId5.getCpf(), insertedStudent.getCpf());
		assertEquals(countTotalStudents + 1, repository.count());
	}

	@Test
	@DisplayName("it should not save a student with the same cpf")
	public void InsertShouldReturnStudentWhenCpfAlreadyExists() {

		// given
		SameCpfStudent = Factory.SameCpfStudent();

		// then
		assertThrows(RegistrationExceptions.class, () -> {
			service.insert(SameCpfStudent);
		});

	}

	@Test
	@DisplayName("Update should update a student")
	public void UpdateShouldReturnStudentWhenIdExist() {
		// given
		LaststudentId1ToUpdate = Factory.createStudentToUpdate();
		NewstudentId1ToUpdate = Factory.createStudent();

		// when
		Student UpdatedstudentId1 = service.update(existingId, NewstudentId1ToUpdate);

		// then
		assertNotNull(UpdatedstudentId1);
		assertEquals(NewstudentId1ToUpdate, UpdatedstudentId1);
		assertEquals(NewstudentId1ToUpdate.getNome(), UpdatedstudentId1.getNome());
		assertNotEquals(LaststudentId1ToUpdate.getNome(), UpdatedstudentId1.getNome());

	}

	@Test
	@DisplayName("update an inexistent id should display an EntityNotFoundException")
	public void UpdateShouldReturnStudentWhenIdDoesNotExist() {

		assertThrows(EntityNotFoundExceptions.class, () -> {
			service.update(nonExistingId, NewstudentId1ToUpdate);
		});

	}

}
