package com.HappySchool.Project.controller;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.HappySchool.Project.entities.Diretor;
import com.HappySchool.Project.entities.Student;
import com.HappySchool.Project.entities.dto.DiretorCreateDTO;
import com.HappySchool.Project.entities.dto.DiretorDTO;
import com.HappySchool.Project.entities.dto.StudentDTO;
import com.HappySchool.Project.services.DiretorService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/diretors")
public class DiretorController {

	@Autowired
	private DiretorService service;

	@GetMapping
	public List<DiretorDTO> findAll() {
		return service.findAll().stream().map(DiretorDTO::from).toList();
	}
	

	@GetMapping(value = "/{matricula}")
	public ResponseEntity<DiretorDTO> findById(@PathVariable Integer matricula) {
		Diretor obj = service.findById(matricula);
		DiretorDTO.from(obj);
		return ResponseEntity.ok().body(DiretorDTO.from(obj));
	}

	@PostMapping
	public ResponseEntity<?> Insert(@Valid @RequestBody DiretorCreateDTO dto, BindingResult result) {
		if (result.hasErrors()) {
			List<String> errors = result.getAllErrors().stream().map(error -> error.getDefaultMessage())
					.collect(Collectors.toList());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
		}

		Diretor savedDiretor = service.insert(dto.toEntity());
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{matricula}")
				.buildAndExpand(savedDiretor.getMatricula()).toUri();
		return ResponseEntity.created(uri).body(savedDiretor);

	}
	@DeleteMapping(value = "/{matricula}")
	public ResponseEntity<Diretor> delete(@PathVariable Integer matricula) {
		service.delete(matricula);
		return ResponseEntity.noContent().build();
	}

	@PutMapping(value = "/{matricula}")
	public ResponseEntity<DiretorDTO> update(@PathVariable Integer matricula, @RequestBody Diretor newDiretor) {

		newDiretor = service.update(matricula, newDiretor);
		return ResponseEntity.ok().body(DiretorDTO.from(newDiretor));

	}

}
