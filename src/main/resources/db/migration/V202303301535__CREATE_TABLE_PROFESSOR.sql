DROP TABLE IF EXISTS tb_professor CASCADE; CREATE TABLE tb_professor (
    matricula SERIAL PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    cpf VARCHAR(11) NOT NULL,
    especialidade VARCHAR(225) NOT NULL
);