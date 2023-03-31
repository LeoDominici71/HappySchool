package com.HappySchool.Project.entities.dto;

import org.hibernate.validator.constraints.br.CPF;

import com.HappySchool.Project.entities.Diretor;

import jakarta.validation.constraints.NotBlank;

public record DiretorCreateDTO(
        @NotBlank
        String nome,

        @NotBlank
        @CPF
        String cpf)

{

    public Diretor toEntity() {
        return new Diretor(null, this.nome, this.cpf);
    }
}
