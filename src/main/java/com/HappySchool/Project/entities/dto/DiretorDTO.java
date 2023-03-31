package com.HappySchool.Project.entities.dto;

import com.HappySchool.Project.entities.Diretor;

public record DiretorDTO(String nome, String cpf) {

	public static DiretorDTO from(Diretor diretor) {
		return new DiretorDTO(diretor.getNome(), diretor.getCpf());
	}

	public static Diretor to(DiretorDTO diretorDTO) {
		return new Diretor(null, diretorDTO.nome(), diretorDTO.cpf());
	}
}