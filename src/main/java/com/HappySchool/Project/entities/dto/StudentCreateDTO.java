package com.HappySchool.Project.entities.dto;

import org.hibernate.validator.constraints.br.CPF;

import com.HappySchool.Project.entities.Student;

import jakarta.validation.constraints.NotBlank;

public record StudentCreateDTO(
        @NotBlank
        String nome,

        @NotBlank
        @CPF
        String cpf) {

    public Student toEntity() {
        return new Student(null ,this.nome, this.cpf);
    }
}