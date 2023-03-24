package com.HappySchool.Project.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.HappySchool.Project.entities.Curso;
import com.HappySchool.Project.entities.Grades;
import com.HappySchool.Project.entities.Student;
import com.HappySchool.Project.entities.dto.GradesDTO;
import com.HappySchool.Project.entities.pk.GradesPK;
import com.HappySchool.Project.repository.CouseRepository;
import com.HappySchool.Project.repository.GradesRepository;
import com.HappySchool.Project.repository.StudentRepository;
import com.HappySchool.Project.servicesException.DataExceptions;
import com.HappySchool.Project.servicesException.EntityNotFoundExceptions;

import jakarta.persistence.EntityNotFoundException;

@Service
public class GradesService {

	@Autowired
	private GradesRepository repository;

	@Autowired
	private CouseRepository cursorepository;

	@Autowired
	private StudentRepository studentrepository;

	public List<Grades> findAll() {
		return repository.findAll();

	}

	public Grades insert(GradesDTO dto) {
		try {
			Integer idCurso = dto.getCourseId();
			Long idStudent = dto.getStudentId();
			Curso curso = cursorepository.findById(idCurso)
					.orElseThrow(() -> new EntityNotFoundExceptions("Curso doesn't exist"));
			Student student = studentrepository.findById(idStudent)
					.orElseThrow(() -> new EntityNotFoundExceptions("Student doesn't exist"));

			Grades grades = new Grades();
			grades.setGrades(dto.getGrades());
			grades.setCurso(curso);
			grades.setStudent(student);

			return repository.save(grades);
		} catch (DataIntegrityViolationException e) {
			throw new DataExceptions("There are Null fields");
		}
	}

	public void delete(Long studentId, Integer courseId) {
	    GradesPK id = new GradesPK(new Student(studentId), new Curso(courseId));
	    repository.findById(id).map(grades -> {
	        repository.deleteById(id);
	        return grades;
	    }).orElseThrow(() -> new EntityNotFoundExceptions("id doesn't exist"));
	}
}


