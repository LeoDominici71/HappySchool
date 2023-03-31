package com.HappySchool.Project.entities.dto;

import org.hibernate.validator.constraints.br.CPF;

import com.HappySchool.Project.entities.Professor;

import jakarta.validation.constraints.NotBlank;

public record ProfessorCreateDTO(
        @NotBlank
        String nome,

        @NotBlank
        @CPF
        String cpf,

        @NotBlank
        String especialidade)

{

    public Professor toEntity() {
        return new Professor(null, this.nome, this.cpf, this.especialidade);
    }
}
