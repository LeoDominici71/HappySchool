package com.HappySchool.Project.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.HappySchool.Project.entities.Curso;
import com.HappySchool.Project.entities.Grades;
import com.HappySchool.Project.entities.Professor;
import com.HappySchool.Project.entities.Student;
import com.HappySchool.Project.entities.dto.GradesDTO;
import com.HappySchool.Project.repository.CouseRepository;
import com.HappySchool.Project.repository.GradesRepository;
import com.HappySchool.Project.repository.StudentRepository;
import com.HappySchool.Project.services.GradesService;
import com.HappySchool.Project.servicesException.EntityNotFoundExceptions;
import com.HappySchool.Project.tests.Factory;

@ExtendWith(SpringExtension.class)
public class GradesServiceTests {

	@InjectMocks
	private GradesService service;

	@Mock
	private GradesRepository repository;

	@Mock
	private CouseRepository CursoRepository;

	@Mock
	private StudentRepository StudentRepository;

	private Integer CourseExistingId;
	private Long StudentExistingId;
	private Long NonExistingStudentId;
	private Integer NonExistingCourseId;
	private List<Grades> Grades;
	private Student student;
	private Curso curso;

	@BeforeEach
	void setUp() throws Exception {

		StudentExistingId = 1L;
		CourseExistingId = 1;
		NonExistingStudentId = 1000L;
		NonExistingCourseId = 1000;
		curso = Factory.createCursoToUpdate();
		student = Factory.createStudent();
		Grades = Arrays.asList(new Grades(curso, student, 9.0), new Grades(curso, student, 9.0));

	}

	@Test
	public void testInsertWithNonExistentStudent() {
		// create input DTO
		GradesDTO dto = new GradesDTO(NonExistingStudentId, CourseExistingId, 9.0);

		// mock repository behavior
		assertThrows(EntityNotFoundExceptions.class, () -> {
			service.insert(dto);
		});

	}

	@Test
	public void testInsertWithNonExistentCurse() {
		// create input DTO
		GradesDTO dto = new GradesDTO(StudentExistingId, NonExistingCourseId, 9.0);

		// mock repository behavior
		assertThrows(EntityNotFoundExceptions.class, () -> {
			service.insert(dto);
		});

	}

	@Test
	public void testInsert() {
		// create input DTO
		GradesDTO dto = new GradesDTO(StudentExistingId, CourseExistingId, 9.0);

		// create mock professor
		Professor professor = new Professor();
		professor.setMatricula(1L);
		professor.setNome("Professor Teste");
		professor.setCpf("82437975055");
		professor.setEspecialidade("Java");

		// create mock Curso
		Curso curso = new Curso(1, "Java", "Java com Spring", professor);

		// create mock Student
		Student student = Factory.createNewStudent();

		// mock repository behavior
		Mockito.when(CursoRepository.findById(1)).thenReturn(Optional.of(curso));
		Mockito.when(StudentRepository.findById(1L)).thenReturn(Optional.of(student));
		Mockito.when(repository.save(Mockito.any())).thenReturn(new Grades());

		// call the method being tested
		Grades result = service.insert(dto);

		// assert the result is not null
		assertNotNull(result);

		// assert the repository methods were called
		Mockito.verify(CursoRepository, Mockito.times(1)).findById(1);
		Mockito.verify(StudentRepository, Mockito.times(1)).findById(1L);
		Mockito.verify(repository, Mockito.times(1)).save(Mockito.any(Grades.class));
	}

	@Test
	@DisplayName("Should return a list of Grades")
	public void testFindAll() {

		// when
		Mockito.when(repository.findAll()).thenReturn(Grades);
		List<Grades> result = service.findAll();

		// then
		assertNotNull(result);
		assertEquals(Grades.size(), result.size());
		assertEquals(Grades, result);
		verify(repository, Mockito.times(1)).findAll();
	}

	@Test
	public void ShouldThrowEntityNotFoundWhenDeleteNonExistingId() {
		// create mock professor
		Professor professor = new Professor();
		professor.setMatricula(1L);
		professor.setNome("Professor Teste");
		professor.setCpf("82437975055");
		professor.setEspecialidade("Java");

		// create mock Curso
		Curso curso = new Curso(1000, "Java", "Java com Spring", professor);

		// create mock Student
		Student student = Factory.createNewStudent();

		// mock repository behavior
		assertThrows(EntityNotFoundExceptions.class, () -> {
			service.delete(student.getMatricula(), curso.getId());
		});

	}

	@Test
	public void ShouldThrowEntityNotFoundWhenDeleteNonExistingIdStudent() {
		// create mock professor
		Professor professor = new Professor();
		professor.setMatricula(1L);
		professor.setNome("Professor Teste");
		professor.setCpf("82437975055");
		professor.setEspecialidade("Java");

		// create mock Curso
		Curso curso = new Curso(1, "Java", "Java com Spring", professor);

		// create mock Student
		Student student = new Student(1000L, "Jane Doe", "70409951820");

		// mock repository behavior
		assertThrows(EntityNotFoundExceptions.class, () -> {
			service.delete(student.getMatricula(), curso.getId());
		});

	}

	@Test
	public void testDelete() {
		// create input DTO
		GradesDTO dto = new GradesDTO(StudentExistingId, CourseExistingId, 9.0);

		// create mock professor
		Professor professor = new Professor();
		professor.setMatricula(1L);
		professor.setNome("Professor Teste");
		professor.setCpf("82437975055");
		professor.setEspecialidade("Java");

		// create mock Curso
		Curso curso = new Curso(1, "Java", "Java com Spring", professor);

		// create mock Student
		Student student = Factory.createNewStudent();

		// create mock Grades
		Grades grades = new Grades(curso, student, dto.getGrades());

		// mock repository behavior

		Mockito.when(repository.findById(Mockito.any())).thenReturn(Optional.of(grades));
		doNothing().when(repository).deleteById(Mockito.any());

		// call the method being tested
		service.delete(dto.getStudentId(), dto.getCourseId());

		// assert the repository methods were called
		Mockito.verify(repository, Mockito.times(1)).findById(Mockito.any());
		Mockito.verify(repository, Mockito.times(1)).deleteById(Mockito.any());

	}

	@Test
	public void testUpdate() {
		// create input DTO
		GradesDTO dto = new GradesDTO(StudentExistingId, CourseExistingId, 9.0);

		// create mock professor
		Professor professor = new Professor();
		professor.setMatricula(1L);
		professor.setNome("Oliveira");
		professor.setCpf("82437975055");
		professor.setEspecialidade("Java");

		// create mock Curso
		Curso curso = new Curso(CourseExistingId, "Java", "Java com Spring", professor);

		// create mock Student
		Student student = new Student();
		student.setMatricula(StudentExistingId);
		student.setNome("Student Teste");

		// create mock Grades
		Grades grades = new Grades(curso, student, dto.getGrades());

		// mock repository behavior
		Mockito.when(repository.findById(Mockito.any())).thenReturn(Optional.of(grades));
		Mockito.when(repository.save(Mockito.any())).thenReturn(grades);

		// create new mock Grades
		Grades newgrades = new Grades(curso, student, 8.0);

		// call the method being tested
		Grades updatedGrades = service.update(StudentExistingId, CourseExistingId, newgrades);

		// assert the repository methods were called
		Mockito.verify(repository, Mockito.times(1)).findById(Mockito.any());
		Mockito.verify(repository, Mockito.times(1)).save(Mockito.any());

		// assert that the output is as expected
		assertEquals(8.0, updatedGrades.getGrades(), 0.001);
	}

	@Test
	public void testUpdateWhenStudentIdDoesNotExist() {

		// create mock professor
		Professor professor = new Professor();
		professor.setMatricula(1L);
		professor.setNome("Oliveira");
		professor.setCpf("82437975055");
		professor.setEspecialidade("Java");

		// create mock Curso
		Curso curso = new Curso(CourseExistingId, "Java", "Java com Spring", professor);

		// create mock Student
		Student student = new Student();
		student.setMatricula(StudentExistingId);
		student.setNome("Student Teste");

		// create new mock Grades
		Grades newgrades = new Grades(curso, student, 8.0);

		// call the method being tested

		// assert the repository methods were called
		assertThrows(EntityNotFoundExceptions.class, () -> {
			service.update(NonExistingStudentId, CourseExistingId, newgrades);
		});

	}
	
	@Test
	public void testUpdateWhenCourseIdDoesNotExist() {

		// create mock professor
		Professor professor = new Professor();
		professor.setMatricula(1L);
		professor.setNome("Oliveira");
		professor.setCpf("82437975055");
		professor.setEspecialidade("Java");

		// create mock Curso
		Curso curso = new Curso(CourseExistingId, "Java", "Java com Spring", professor);

		// create mock Student
		Student student = new Student();
		student.setMatricula(StudentExistingId);
		student.setNome("Student Teste");

		// create new mock Grades
		Grades newgrades = new Grades(curso, student, 8.0);

		// call the method being tested

		// assert the repository methods were called
		assertThrows(EntityNotFoundExceptions.class, () -> {
			service.update(StudentExistingId, NonExistingCourseId, newgrades);
		});

	}
}
