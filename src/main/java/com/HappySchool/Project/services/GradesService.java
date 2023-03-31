package com.HappySchool.Project.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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

@Service
public class GradesService {

	@Autowired
	private final GradesRepository repository;

	@Autowired
	private final CouseRepository cursorepository;

	@Autowired
	private final StudentRepository studentrepository;

	public GradesService(GradesRepository repository, CouseRepository cursorepository,
			StudentRepository studentrepository) {
		this.repository = repository;
		this.cursorepository = cursorepository;
		this.studentrepository = studentrepository;
	}

	public List<Grades> findAll() {
		return repository.findAll();

	}

	public Grades findById(Long studentId, Integer courseId) {
		GradesPK id = new GradesPK(new Student(studentId), new Curso(courseId));
		Grades grades = repository.findById(id).orElseThrow(() -> new EntityNotFoundExceptions("Student or Curso doesn't exist"));
		return grades;
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

	public Grades update(Long studentId, Integer courseId, Grades newGrades) {
		GradesPK id = new GradesPK(new Student(studentId), new Curso(courseId));
		return repository.findById(id).map(grades -> {
			grades.setGrades(newGrades.getGrades());
			return repository.save(grades);
		}).orElseThrow(() -> new EntityNotFoundExceptions("id doesn't exist"));
	}

}
