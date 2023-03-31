package com.HappySchool.Project.entities.dto;

import com.HappySchool.Project.entities.Student;

public record StudentDTO(
        String nome,
        String cpf
) {

    public static StudentDTO from(Student student) {
        return new StudentDTO(student.getNome(), student.getCpf());
    }

    public static Student to(StudentDTO studentDTO) {
        return new Student(null ,studentDTO.nome(), studentDTO.cpf());
    }
}