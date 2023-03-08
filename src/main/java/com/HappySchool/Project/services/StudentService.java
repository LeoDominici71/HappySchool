package com.HappySchool.Project.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.HappySchool.Project.entities.Student;
import com.HappySchool.Project.repository.StudentRepository;
import com.HappySchool.Project.servicesException.EntityNotFoundExceptions;

import jakarta.persistence.EntityNotFoundException;

@Service
public class StudentService {

	@Autowired
	private StudentRepository repository;

	public List<Student> findAll() {
		return repository.findAll();

	}

	public Student findById(Integer matricula) {
		Optional<Student> obj = repository.findById(matricula);
		return obj.get();
	}

	public boolean cpfExists(String cpf) {
		Optional<Student> StudentOptional = repository.findByCpf(cpf);
		return StudentOptional.isPresent();
	}

	public Student insert(Student obj) {
		return repository.save(obj);

	}

	public void delete(Integer matricula) {
		repository.findById(matricula).map(Student -> {
			repository.delete(Student);
			return Void.TYPE;
		}).orElseThrow(() -> new EntityNotFoundExceptions("Matricula: " + matricula + " doesn't exist"));

	}

	public Student update(Integer matricula, Student upstudent) {
		try {
			Student entity = repository.getReferenceById(matricula);
			entity.setNome(upstudent.getNome());
			return repository.save(entity);
		} catch (EntityNotFoundException e) {
			throw new EntityNotFoundExceptions("Matricula: " + matricula + " doesn't exist");
		}
	}

}
