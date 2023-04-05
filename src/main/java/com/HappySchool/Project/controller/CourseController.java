package com.HappySchool.Project.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.HappySchool.Project.entities.Curso;
import com.HappySchool.Project.entities.dto.CursoDTO;
import com.HappySchool.Project.services.CourseService;

@RestController
@RequestMapping(value = "/cursos")
@CrossOrigin("http://localhost:4200")
public class CourseController {

	@Autowired
	private CourseService service;

	@GetMapping
	public ResponseEntity<List<Curso>> findAll() {
		List<Curso> list = service.findAll();
		return ResponseEntity.ok().body(list);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<Curso> findById(@PathVariable Integer id) {
		Curso obj = service.findById(id);
		return ResponseEntity.ok().body(obj);
	}

	@PostMapping
	public ResponseEntity<Curso> insert(@RequestBody CursoDTO curso) {
		Curso obj = service.insert(curso);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).body(obj);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Curso> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<Curso> update(@PathVariable Integer id, @RequestBody Curso newCurso) {

		newCurso = service.update(id, newCurso);
		return ResponseEntity.ok().body(newCurso);

	}

}
