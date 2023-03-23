package com.HappySchool.Project.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.HappySchool.Project.entities.Curso;
import com.HappySchool.Project.entities.Professor;
import com.HappySchool.Project.entities.dto.CursoDTO;
import com.HappySchool.Project.repository.CouseRepository;
import com.HappySchool.Project.repository.ProfessorRepository;
import com.HappySchool.Project.servicesException.DataExceptions;
import com.HappySchool.Project.servicesException.EntityNotFoundExceptions;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CourseService {

	@Autowired
	private final CouseRepository repository;

	@Autowired
	private final ProfessorRepository Profrepository;

	public CourseService(CouseRepository repository, ProfessorRepository profrepository) {
		this.repository = repository;
		this.Profrepository = profrepository;

	}

	public List<Curso> findAll() {
		return repository.findAll();

	}

	public Curso findById(Integer id) {
		return repository.findById(id)
				.orElseThrow(() -> new EntityNotFoundExceptions("Course " + id + " doesn't exist"));

	}

	public Curso insert(CursoDTO dto) {
		try {
			Long idProfessor = dto.getProfessorId();
			Professor professor = Profrepository.findById(idProfessor)
					.orElseThrow(() -> new EntityNotFoundExceptions("Professor doesn't exist"));
			Curso curso = new Curso();
			curso.setDescricao(dto.getDescricao());
			curso.setNome(dto.getNome());
			curso.setProfessor(professor);

			return repository.save(curso);
		} catch (DataIntegrityViolationException e) {
			throw new DataExceptions("There are Null fields");
		}

	}

	public void delete(Integer id) {
		repository.findById(id).map(Curso -> {
			repository.delete(Curso);
			return Void.TYPE;
		}).orElseThrow(() -> new EntityNotFoundExceptions("Course: " + id + " doesn't exist"));
	}

	public Curso update(Integer id, Curso upCurso) {
	    return repository.findById(id)
	        .map(entity -> {
	            entity.setNome(upCurso.getNome());
	            entity.setDescricao(upCurso.getDescricao());
	            return repository.save(entity);
	        })
	        .orElseThrow(() -> new EntityNotFoundExceptions("Course: " + id + " doesn't exist"));
	}
}

