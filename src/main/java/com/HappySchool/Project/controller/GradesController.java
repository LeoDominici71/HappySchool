package com.HappySchool.Project.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.HappySchool.Project.entities.Grades;
import com.HappySchool.Project.entities.dto.GradesDTO;
import com.HappySchool.Project.services.GradesService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/grades")
public class GradesController {

	@Autowired
	private GradesService service;

	@GetMapping
	public ResponseEntity<List<Grades>> findAll() {
		List<Grades> list = service.findAll();
		return ResponseEntity.ok().body(list);
	}
	
	@PostMapping
	public ResponseEntity<Grades> insert(@RequestBody @Valid GradesDTO grades) {
		Grades obj = service.insert(grades);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{studentId}/{courseId}").buildAndExpand(obj.getStudent().getMatricula(), obj.getCurso().getId()).toUri();
		return ResponseEntity.created(uri).body(obj);
	}
	
	@DeleteMapping(value = "/{studentId}/{courseId}")
	public ResponseEntity<Grades> delete(@PathVariable("studentId") Long studentId, @PathVariable("courseId") Integer courseId) {
		service.delete(studentId,courseId);
		return ResponseEntity.noContent().build();
	}


}
