package com.HappySchool.Project.controller;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.HappySchool.Project.entities.Student;
import com.HappySchool.Project.entities.dto.StudentCreateDTO;
import com.HappySchool.Project.entities.dto.StudentDTO;
import com.HappySchool.Project.services.StudentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/students")
public class StudentController {

	private final StudentService service;

	public StudentController(StudentService service) {
		this.service = service;
	}

	@GetMapping
	public List<StudentDTO> findAll() {
		return service.findAll().stream().map(StudentDTO::from).toList();
	}

	@GetMapping(value = "/{matricula}")
	public ResponseEntity<StudentDTO> findById(@PathVariable Long matricula) {
		Student obj = service.findById(matricula);
		StudentDTO.from(obj);
		return ResponseEntity.ok().body(StudentDTO.from(obj));
	}

	@PostMapping
	public ResponseEntity<?> Insert(@Valid @RequestBody StudentCreateDTO dto, BindingResult result) {
		if (result.hasErrors()) {
			List<String> errors = result.getAllErrors().stream().map(error -> error.getDefaultMessage())
					.collect(Collectors.toList());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
		}

		Student savedStudent = service.insert(dto.toEntity());
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{matricula}")
				.buildAndExpand(savedStudent.getMatricula()).toUri();
		return ResponseEntity.created(uri).body(savedStudent);

	}

	@DeleteMapping(value = "/{matricula}")
	public ResponseEntity<Student> delete(@PathVariable Long matricula) {
		service.delete(matricula);
		return ResponseEntity.noContent().build();
	}

	@PutMapping(value = "/{matricula}")
	public ResponseEntity<StudentDTO> update(@PathVariable Long matricula, @RequestBody Student newStudent) {

		newStudent = service.update(matricula, newStudent);
		return ResponseEntity.ok().body(StudentDTO.from(newStudent));

	}

}
