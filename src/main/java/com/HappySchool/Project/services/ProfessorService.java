package com.HappySchool.Project.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.HappySchool.Project.entities.Professor;
import com.HappySchool.Project.repository.ProfessorRepository;
import com.HappySchool.Project.servicesException.DatabaseExceptions;
import com.HappySchool.Project.servicesException.EntityNotFoundExceptions;
import com.HappySchool.Project.servicesException.MethodArgumentNotValidExceptions;
import com.HappySchool.Project.servicesException.RegistrationExceptions;

@Service
public class ProfessorService {

	private final ProfessorRepository repository;

	public ProfessorService(ProfessorRepository repository) {
		this.repository = repository;

	}

	public List<Professor> findAll() {
		return repository.findAll();

	}

	public Professor findById(Long matricula) {
		return repository.findById(matricula)
				.orElseThrow(() -> new EntityNotFoundExceptions("Matricula doesn't exist"));
	}

	public boolean cpfExists(String cpf) {
		Optional<Professor> ProfessorOptional = repository.findByCpf(cpf);
		return ProfessorOptional.isPresent();
	}

	public Professor insert(Professor obj) throws RegistrationExceptions {
		if (cpfExists(obj.getCpf())) {
			throw new RegistrationExceptions("This CPF already exist");
		}
		return repository.save(obj);

	}

	public void delete(Long matricula) {
		try {
			repository.findById(matricula).map(Professor -> {
				repository.delete(Professor);
				return Void.TYPE;
			}).orElseThrow(() -> new EntityNotFoundExceptions("Professor not found"));
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseExceptions("Cannot execute this action");
		}
	}
	
	

	public Professor update(Long matricula, Professor Professor) {
		return repository.findById(matricula).map(Professors -> {
			Professors.setNome(Professor.getNome());
			return repository.save(Professors);
		}).orElseThrow(() -> new EntityNotFoundExceptions("Professor not found"));

	}
}
