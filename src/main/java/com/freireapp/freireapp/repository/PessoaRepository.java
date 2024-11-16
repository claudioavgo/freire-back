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
public class PessoaRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> getAllPessoas() {
        String sql = "SELECT * FROM Pessoa";
        return jdbcTemplate.queryForList(sql);
    }

    public Map<String, Object> getPessoaById(Long id) {
        String sql = "SELECT * FROM Pessoa WHERE id_pessoa = ?";
        return jdbcTemplate.queryForMap(sql, id);
    }

    public Pessoa getPessoaEmail(String email) {
        try {
            String sql = "SELECT * FROM Pessoa WHERE email = ?";
            return jdbcTemplate.queryForObject(sql, new Object[]{email},
                    new BeanPropertyRowMapper<>(Pessoa.class));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado.");
        }
    }
    public boolean eAluno(int id) {
        try {
            String sql = "SELECT * FROM Aluno WHERE fk_Pessoa_id_pessoa = ?";
            return !jdbcTemplate.queryForMap(sql,id).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    public Map<String, Object> getPessoaByEmail(String email) {
        String sql = "SELECT * FROM Pessoa WHERE email = ?";
        try {
            return jdbcTemplate.queryForMap(sql, email);
        } catch (Exception e) {
            return null;
        }
    }

    public Map<String, Object> getPessoaByTelefone(String telefone) {
        String sql = "SELECT * FROM Pessoa WHERE telefone_1 = ?";
        try {
            return jdbcTemplate.queryForMap(sql, telefone);
        } catch (Exception e) {
            return null;
        }
    }

    public List<Map<String, Object>> getListaProfessores() {
        String sql = "SELECT p.nome, p2.especializacao FROM Pessoa p JOIN Professor p2 ON p.id_pessoa = p2.fk_Pessoa_id_pessoa";
        return jdbcTemplate.queryForList(sql);
    }
}
