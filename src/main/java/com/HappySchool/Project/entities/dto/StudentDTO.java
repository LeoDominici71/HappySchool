package com.HappySchool.Project.entities.dto;

import com.HappySchool.Project.entities.Student;

public record StudentDTO(
		Long matricula,
        String nome,
        String cpf
) {

    public static StudentDTO from(Student student) {
        return new StudentDTO(student.getMatricula(),student.getNome(), student.getCpf());
    }

    public static Student to(StudentDTO studentDTO) {
        return new Student(studentDTO.matricula() ,studentDTO.nome(), studentDTO.cpf());
    }
}