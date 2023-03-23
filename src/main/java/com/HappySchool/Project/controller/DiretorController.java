package com.HappySchool.Project.controller;

import java.net.URI;
import java.util.List;

import com.HappySchool.Project.services.DiretorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
import com.HappySchool.Project.servicesException.RegistrationExceptions;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/Diretors")
public class DiretorController {

	@Autowired
	private DiretorService service;

	@GetMapping
	public ResponseEntity<List<Diretor>> findAll() {
		List<Diretor> list = service.findAll();
		return ResponseEntity.ok().body(list);
	}

	@GetMapping(value = "/{matricula}")
	public ResponseEntity<Diretor> findById(@PathVariable Integer matricula) {
		Diretor obj = service.findById(matricula);
		return ResponseEntity.ok().body(obj);
	}

	@PostMapping
	public ResponseEntity<?> insert(@RequestBody @Valid Diretor Diretor) {
		if (service.cpfExists(Diretor.getCpf())) {
			throw new RegistrationExceptions("This CPF already exist");
		}
		Diretor obj = service.insert(Diretor);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{matricula}")
				.buildAndExpand(obj.getMatricula()).toUri();
		return ResponseEntity.created(uri).body(obj);
	}

	@DeleteMapping(value = "/{matricula}")
	public ResponseEntity<Diretor> delete(@PathVariable Integer matricula) {
		service.delete(matricula);
		return ResponseEntity.noContent().build();
	}

	@PutMapping(value = "/{matricula}")
	public ResponseEntity<?> update(@PathVariable Integer matricula, @RequestBody Diretor newDiretor) {

		newDiretor = service.update(matricula, newDiretor);
		return ResponseEntity.ok().body(newDiretor);

	}

}
