package com.freireapp.freireapp.repository;

import com.freireapp.freireapp.pessoa.Pessoa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Repository
public class ProfessorRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> getAlunos(Long idProfessor) {
        String sql = "SELECT DISTINCT p.nome AS nome_aluno, p.id_pessoa AS id_aluno, " +
                "al.matricula, al.indice_rendimento " +
                "FROM Aula au " +
                "JOIN Aluno al ON al.fk_Pessoa_id_pessoa = au.fk_Aluno_fk_Pessoa_id_pessoa " +
                "JOIN Pessoa p ON al.fk_Pessoa_id_pessoa = p.id_pessoa " +
                "WHERE au.fk_Professor_fk_Pessoa_id_pessoa = ?";
        return jdbcTemplate.queryForList(sql, idProfessor);
    }
}