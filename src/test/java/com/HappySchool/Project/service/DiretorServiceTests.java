package com.HappySchool.Project.service;

import com.HappySchool.Project.entities.Diretor;
import com.HappySchool.Project.repository.DiretorRepository;
import com.HappySchool.Project.services.DiretorService;
import com.HappySchool.Project.servicesException.DatabaseExceptions;
import com.HappySchool.Project.servicesException.EntityNotFoundExceptions;
import com.HappySchool.Project.servicesException.RegistrationExceptions;
import com.HappySchool.Project.tests.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class DiretorServiceTests {

	@InjectMocks
	private DiretorService service;

	@Mock
	private DiretorRepository repository;

	private Integer existingId;
	private Integer nonExistingId;
	private List<Diretor> Diretors;
	private Diretor Diretor;
	private Diretor DiretorToUpdate;
	private Diretor SameCpfDiretor;
	private Diretor SameCpfDiretor1;
	private String existingCpf;
	private Integer dependentId;

	@BeforeEach
	void setUp() throws Exception {
		dependentId = 2;
		existingId = 1;
		nonExistingId = 1000;
		existingCpf = "25331095097";
		SameCpfDiretor = Factory.SameCpfDirector();
		SameCpfDiretor1 = Factory.SameCpfDirector();
		Diretor = Factory.createNewDirector();
		DiretorToUpdate = Factory.createDiretorToUpdate();
		Diretors = Arrays.asList(new Diretor(null, "John", "48374255854"), new Diretor(null, "Jane", "70409951820"));

	}

	@Test
	@DisplayName("it should throw exception when exist a Diretor with the same cpf")
	public void InsertShouldNotReturnDiretorWhenCpfExists() {

		// when
		when(repository.findByCpf(existingCpf)).thenReturn(Optional.of(SameCpfDiretor));

		// then
		assertThrows(RegistrationExceptions.class, () -> {
			service.insert(SameCpfDiretor1);
		});
		verify(repository, never()).save(any());
	}

	@Test
	@DisplayName("it should save a Diretor")
	public void InsertShouldReturnDiretorWhenIdExists() {
		// when
		when(repository.save(any())).thenReturn(Diretor);
		Diretor savedDiretor = service.insert(Diretor);

		// then
		assertNotNull(savedDiretor);
		assertEquals(Diretor, savedDiretor);
		verify(repository, Mockito.times(1)).save(Diretor);
	}

	@Test
	@DisplayName("it should update a Diretor")
	public void UpdateShouldReturnDiretorWhenIdExist() {

		// when
		when(repository.findById(existingId)).thenReturn(Optional.of(Diretor));
		when(repository.save(any())).thenReturn(Diretor);
		Diretor DiretorAlreadyupdated = service.update(existingId, DiretorToUpdate);

		// then
		assertNotNull(DiretorAlreadyupdated);
		assertEquals(Diretor, DiretorAlreadyupdated);
		assertEquals(DiretorToUpdate.getNome(), DiretorAlreadyupdated.getNome());
		verify(repository, Mockito.times(1)).findById(existingId);
		verify(repository, Mockito.times(1)).save(Diretor);

	}

	@Test
	@DisplayName("update an inexistent id should display an EntityNotFoundException")
	public void UpdateShouldReturnDiretorWhenIdDoesNotExist() {

		// when
		when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

		// then
		assertThrows(EntityNotFoundExceptions.class, () -> {
			service.update(nonExistingId, Diretor);
		});
		verify(repository, Mockito.times(1)).findById(nonExistingId);
		verify(repository, Mockito.never()).save(any());
	}

	@Test
	@DisplayName("It should thrown EntityNotFoundException")
	public void findByIdShouldReturnDiretorWhenIdDoesNotExist() {
		// when
		Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

		// then
		assertThrows(EntityNotFoundExceptions.class, () -> {
			service.findById(nonExistingId);
		});
		verify(repository, Mockito.times(1)).findById(nonExistingId);
	}

	@Test
	@DisplayName("It should return a Diretor by Id")
	public void findByIdShouldReturnDiretorWhenIdExist() {
		// when
	    when(repository.findById(existingId)).thenReturn(Optional.of(Diretor));
		Diretor result = service.findById(existingId);

		// then
		assertNotNull(result);
		assertEquals(Diretor, result);
		verify(repository, Mockito.times(1)).findById(existingId);
	}

	@Test
	@DisplayName("Should return a list of Diretors")
	public void testFindAll() {

		// when
		Mockito.when(repository.findAll()).thenReturn(Diretors);
		List<Diretor> result = service.findAll();

		// then
		assertNotNull(result);
		assertEquals(Diretors.size(), result.size());
		assertEquals(Diretors, result);
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
		Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(Diretor));
		service.delete(existingId);

		// then
		verify(repository, Mockito.times(1)).findById(existingId);
		verify(repository, Mockito.times(1)).delete(Diretor);
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
