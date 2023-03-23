package com.HappySchool.Project.tests;

import com.HappySchool.Project.entities.Curso;
import com.HappySchool.Project.entities.Diretor;
import com.HappySchool.Project.entities.Professor;
import com.HappySchool.Project.entities.Student;
import com.HappySchool.Project.entities.dto.CursoDTO;

public class Factory {
	
	//Factory for Student

	public static Student createStudent() {
		Student student = new Student(1L, "Jane Doe", "70409951820");
		return student;
	}

	public static Student createStudentNewStudentToUpadate() {
		Student student1 = new Student(1L, "Jane", "70409951820");
		return student1;
	}
	public static Student createStudentToUpdate() {
		Student student2 = new Student(1L, "Maria Brown", "48374255854");
		return student2;
	}

	public static Student createNewStudent() {
		Student newstudent = new Student(1L, "Jane Doe", "48374255854");
		return newstudent;
	}

	public static Student SameCpfStudent() {
		Student sameCpfstudent = new Student(1L, "Maria Brown", "48374255854");
		return sameCpfstudent;
	}

	public static Student CreateStudent5() {
		Student sameCpfstudent = new Student(5L, "Maria", "33457137056");
		return sameCpfstudent;
	}
	
	
	//Factory for Professor
	

	public static Professor createNewProfessor() {
		Professor newCpfProfessor = new Professor(4L, "Maria", "33457137056", "Java");		
		return newCpfProfessor;
	}

	public static Professor SameCpfProfessor() {
		Professor sameCpfProfessor = new Professor(1L, "Maria", "33457137056", "Java");
		return sameCpfProfessor;
	}

	public static Professor createProfessor() {
		Professor createProfessor = new Professor(1L, "Joao", "48374255854", "Java");		
		return  createProfessor;
	}
	public static Professor createProfessorToUpdate() {
		Professor Professor = new Professor(3L, "Maria Brown", "48374255854", "Java");
		return Professor;
	}
	
	public static Professor createProfessorId3() {
		Professor createProfessor = new Professor(3L, "Joao", "48374255854", "Java");		
		return  createProfessor;
	}


	//Factory for Director


	public static Diretor createNewDirector() {
		Diretor NewCpfDiretor = new Diretor(null, "Carlos", "25331095097" );
		return NewCpfDiretor;
	}

	public static Diretor SameCpfDirector() {
		Diretor SameCpfDiretor = new Diretor(null, "Carlos", "25331095097" );
		return SameCpfDiretor;
	}


	public static Diretor createDiretorToUpdate() {
		Diretor Diretor = new Diretor(null, "Carlos", "75571745002" );
		return Diretor;
	}
	
	
	//Factory for Curso
	
	public static Curso createCursoToUpdate() {
		Curso Diretor = new Curso(null, "Java", "Java com Spring boot", createProfessorId3());
		return Diretor;
	}
	
	public static CursoDTO createCursoDto() {
	CursoDTO cursoDto = new CursoDTO("Java", "Java com Spring", 2L);
	return cursoDto;
	}


}
