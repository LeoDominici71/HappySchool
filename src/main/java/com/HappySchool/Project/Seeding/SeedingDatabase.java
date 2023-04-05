package com.HappySchool.Project.Seeding;

import java.util.Arrays;

import com.HappySchool.Project.entities.*;
import com.HappySchool.Project.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class SeedingDatabase implements CommandLineRunner {


    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private CouseRepository cursoRepository;

    @Autowired
    private GradesRepository gradesRepository;

    @Autowired
    private DiretorRepository directorRepository;

    public SeedingDatabase(StudentRepository studentRepository, ProfessorRepository professorRepository, CouseRepository cursoRepository, GradesRepository gradesRepository, DiretorRepository directorRepository) {
        this.studentRepository = studentRepository;
        this.professorRepository = professorRepository;
        this.cursoRepository = cursoRepository;
        this.gradesRepository = gradesRepository;
        this.directorRepository = directorRepository;
    }

    @Override
    public void run(String... args) {

        Student st1 = new Student(null, "Maria Brown", "48374255854");
        Student st2 = new Student(null, "Alex Green", "70409951820");
        Student st3 = new Student(null, "Alex", "89192344003");
        Student st4 = new Student(4L, "Oliver", "71308435002");
        Student st5 = new Student(5L, "Oliver", "49366901808");

        Professor pf1 = new Professor(null, "Marcos", "48374255854", "Java");
        Professor pf2 = new Professor(null, "Oliveira", "70409951820", "Python");
        Professor pf3 = new Professor(null, "Diogo", "62557980030", "Python");

        Curso c1 = new Curso(null, "Java", "Java com Spring", pf1);
        Curso c2 = new Curso(null, "Python", "Python com Jupyter", pf2);

        Diretor d1 = new Diretor(null, "Carlos", "75571745002");
        Diretor d2 = new Diretor(null, "Isak", "01383347069");

        Grades g1 = new Grades(c1, st2, 9.0);
        Grades g2 = new Grades(c1, st4, 9.0);

        studentRepository.saveAll(Arrays.asList(st1, st2, st3, st4,st5));

        professorRepository.saveAll(Arrays.asList(pf1, pf2, pf3));

        directorRepository.saveAll(Arrays.asList(d1, d2));

        cursoRepository.saveAll(Arrays.asList(c1, c2));

        gradesRepository.saveAll(Arrays.asList(g1, g2));

    }
}
