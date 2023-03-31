DROP TABLE IF EXISTS tb_curso CASCADE;CREATE TABLE IF NOT EXISTS tb_curso (
  id SERIAL PRIMARY KEY,
  nome VARCHAR(255) NOT NULL,
  descricao VARCHAR(255) NOT NULL,
  professor_id INT,
  FOREIGN KEY (professor_id) REFERENCES tb_professor(matricula)
);

DROP TABLE IF EXISTS tb_grades; CREATE TABLE tb_grades (
    curso_id INTEGER NOT NULL,
    student_id INTEGER NOT NULL,
    grades DOUBLE PRECISION NOT NULL,
    PRIMARY KEY (curso_id, student_id),
    FOREIGN KEY (curso_id) REFERENCES tb_curso(id),
    FOREIGN KEY (student_id) REFERENCES tb_students(matricula)
);