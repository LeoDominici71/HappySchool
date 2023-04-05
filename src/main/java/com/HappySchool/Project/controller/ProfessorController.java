package com.HappySchool.Project.controller;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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

import com.HappySchool.Project.entities.Professor;
import com.HappySchool.Project.entities.dto.ProfessorCreateDTO;
import com.HappySchool.Project.entities.dto.ProfessorDTO;
import com.HappySchool.Project.services.ProfessorService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/professors")
@CrossOrigin("http://localhost:4200")
public class ProfessorController {

	@Autowired
	private ProfessorService service;

	@GetMapping // GETS
	public List<ProfessorDTO> findAll() {
		return service.findAll().stream().map(ProfessorDTO::from).toList();
	}

	@GetMapping(value = "/{matricula}")
	public ResponseEntity<ProfessorDTO> findById(@PathVariable Long matricula) {
		Professor obj = service.findById(matricula);
		ProfessorDTO.from(obj);
		return ResponseEntity.ok().body(ProfessorDTO.from(obj));
	}

	@PostMapping
	public ResponseEntity<?> Insert(@Valid @RequestBody ProfessorCreateDTO dto) {
		
		Professor savedProfessor = service.insert(dto.toEntity());
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{matricula}")
				.buildAndExpand(savedProfessor.getMatricula()).toUri();
		return ResponseEntity.created(uri).body(savedProfessor);

	}

	@DeleteMapping(value = "/{matricula}")
	public ResponseEntity<Professor> delete(@PathVariable Long matricula) {
		service.delete(matricula);
		return ResponseEntity.noContent().build();
	}

	@PutMapping(value = "/{matricula}")
	public ResponseEntity<ProfessorDTO> update(@PathVariable Long matricula, @RequestBody Professor newProfessor) {

		newProfessor = service.update(matricula, newProfessor);
		return ResponseEntity.ok().body(ProfessorDTO.from(newProfessor));

	}

}
