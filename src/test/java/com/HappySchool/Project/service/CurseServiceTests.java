package com.HappySchool.Project.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import com.HappySchool.Project.entities.Professor;
import com.HappySchool.Project.entities.dto.CursoDTO;
import com.HappySchool.Project.repository.CouseRepository;
import com.HappySchool.Project.repository.ProfessorRepository;
import com.HappySchool.Project.services.CourseService;
import com.HappySchool.Project.servicesException.DatabaseExceptions;
import com.HappySchool.Project.servicesException.EntityNotFoundExceptions;
import com.HappySchool.Project.tests.Factory;

@ExtendWith(SpringExtension.class)
public class CurseServiceTests {

	@InjectMocks
	private CourseService service;

	@Mock
	private CouseRepository repository;

	@Mock
	private ProfessorRepository ProfessorRepository;

	private Integer existingId;
	private Integer nonExistingId;
	private List<Curso> Cursos;
	private Curso Curso;
	private Curso CursoToUpdate;
	private Integer dependentId;
	private Professor ProfessorSample;
	private Professor ProfessorSample1;
	private CursoDTO CursoDto;
	private Professor ProfessorId;

	@BeforeEach
	void setUp() throws Exception {
		dependentId = 2;
		existingId = 1;
		nonExistingId = 1000;
		Curso = new Curso(null, "Java", "Java com Spring boot", ProfessorSample);
		CursoToUpdate = Factory.createCursoToUpdate();
		ProfessorSample = Factory.createNewProfessor();
		ProfessorSample1 = Factory.createProfessorId3();
		CursoDto = Factory.createCursoDto();
		Cursos = Arrays.asList(new Curso(null, "Java", "Java com Spring boot", ProfessorSample),
				new Curso(null, "Python", "Python Jupyter", ProfessorSample1));

	}
	
	@Test
	  public void testInsertWithNonExistentProfessor() {
	    // create input DTO
	    CursoDTO dto = new CursoDTO();
	    dto.setDescricao("Spring");
	    dto.setNome("Java");
	    dto.setProfessorId(10000L);

	    // mock repository behavior
	    assertThrows(EntityNotFoundExceptions.class, () -> {
	    	 service.insert(dto);
	    	 });
	   
	  }

	@Test
	  public void testInsert() {
	    // create input DTO
	    CursoDTO dto = new CursoDTO();
	    dto.setDescricao("curso de Spring");
	    dto.setNome("Java");
	    dto.setProfessorId(1L);

	    // create mock professor
	    Professor professor = new Professor();
	    professor.setMatricula(1L);
	    professor.setNome("Professor Teste");
	    professor.setCpf("82437975055");
	    professor.setEspecialidade("Java");

	    // mock repository behavior
	    Mockito.when(ProfessorRepository.findById(1L)).thenReturn(Optional.of(professor));
	    Mockito.when(repository.save(Mockito.any(Curso.class))).thenReturn(new Curso());

	    // call the method being tested
	    Curso result = service.insert(dto);

	    // assert the result is not null
	    assertNotNull(result);

	    // assert the repository methods were called
	    Mockito.verify(ProfessorRepository, Mockito.times(1)).findById(1L);
	    Mockito.verify(repository, Mockito.times(1)).save(Mockito.any(Curso.class));
	  }

	@Test
	@DisplayName("it should update a Curso")
	public void UpdateShouldReturnCursoWhenIdExist() {

		// when
		when(repository.findById(existingId)).thenReturn(Optional.of(Curso));
		when(repository.save(any())).thenReturn(Curso);
		Curso CursoAlreadyupdated = service.update(existingId, CursoToUpdate);

		// then
		assertNotNull(CursoAlreadyupdated);
		assertEquals(Curso, CursoAlreadyupdated);
		assertEquals(CursoToUpdate.getNome(), CursoAlreadyupdated.getNome());
		verify(repository, Mockito.times(1)).findById(existingId);
		verify(repository, Mockito.times(1)).save(Curso);

	}

	@Test
	@DisplayName("update an inexistent id should display an EntityNotFoundException")
	public void UpdateShouldReturnCursoWhenIdDoesNotExist() {

		// when
		when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

		// then
		assertThrows(EntityNotFoundExceptions.class, () -> {
			service.update(nonExistingId, Curso);
		});
		verify(repository, Mockito.times(1)).findById(nonExistingId);
		verify(repository, Mockito.never()).save(any());
	}

	@Test
	@DisplayName("It should thrown EntityNotFoundException")
	public void findByIdShouldReturnCursoWhenIdDoesNotExist() {
		// when
		Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

		// then
		assertThrows(EntityNotFoundExceptions.class, () -> {
			service.findById(nonExistingId);
		});
		verify(repository, Mockito.times(1)).findById(nonExistingId);
	}

	@Test
	@DisplayName("It should return a Curso by Id")
	public void findByIdShouldReturnCursoWhenIdExist() {
		// when
	    when(repository.findById(existingId)).thenReturn(Optional.of(Curso));
		Curso result = service.findById(existingId);

		// then
		assertNotNull(result);
		assertEquals(Curso, result);
		verify(repository, Mockito.times(1)).findById(existingId);
	}

	@Test
	@DisplayName("Should return a list of Cursos")
	public void testFindAll() {

		// when
		when(repository.findById(existingId)).thenReturn(Optional.of(Curso));
		Mockito.when(repository.findAll()).thenReturn(Cursos);
		List<Curso> result = service.findAll();

		// then
		assertNotNull(result);
		assertEquals(Cursos.size(), result.size());
		assertEquals(Cursos, result);
		verify(repository, Mockito.times(1)).findAll();
	}

	@Test
	@DisplayName("Delete should thrown EntityNotFoundException")
	public void deleteShouldThrownEntityNotFoundExceptionWhenIdNotExists() {

		// when
		when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

		// then
		assertThrows(EntityNotFoundExceptions.class, () -> {
			service.delete(nonExistingId);
		});
		verify(repository, Mockito.times(1)).findById(nonExistingId);
		verify(repository, Mockito.never()).delete(any());
	}

	@Test
	@DisplayName("Delete should throw nothing")
	public void deleteShouldDoNothingWhenIdExists() {

		// when
		Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(Curso));
		service.delete(existingId);

		// then
		verify(repository, Mockito.times(1)).findById(existingId);
		verify(repository, Mockito.times(1)).delete(Curso);
	}

	@Test
	@DisplayName("Delete should thrown DatabaseException")
	public void deleteShouldThrownDatabaseExceptionWhenIdisDependent() {
		// when
		doThrow(DatabaseExceptions.class).when(repository).findById(dependentId);
		// then
		assertThrows(DatabaseExceptions.class, () -> {
			service.delete(dependentId);
		});
		verify(repository, Mockito.times(1)).findById(dependentId);
		verify(repository, Mockito.never()).delete(any());
	}

}
