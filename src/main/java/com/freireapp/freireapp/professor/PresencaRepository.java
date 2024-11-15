package com.freireapp.freireapp.professor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public class PresencaRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void registrarChamada(Long idAluno, Boolean status, LocalDate data) {
        String sql = "INSERT INTO Presenca (data, status, fk_Aluno_fk_Pessoa_id_pessoa, fk_Professor_fk_Pessoa_id_pessoa) VALUES (?, ?, ?, ?)";
        int idProfessor = 13; // id para teste

        jdbcTemplate.update(sql, data, status, idAluno, idProfessor);
    }
}