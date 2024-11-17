package com.freireapp.freireapp.repository;

import com.freireapp.freireapp.dto.CadastroDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class SecretariaRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PessoaRepository pessoaRepository;

    public SecretariaRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void cadastrarPessoa(CadastroDTO data) {
        String sql = "INSERT INTO Pessoa (nome, rua, numero, cidade, telefone_1, telefone_2, email, senha, data_nascimento) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
            data.nome(),
            data.rua(),
            data.numero(),
            data.cidade(),
            data.telefone1(),
            data.telefone2(),
            data.email(),
            data.senha(),
            data.dataNascimento()
        );
    }

    public void cadastrarAluno(CadastroDTO data) {
        Map<String, Object> pessoaData = pessoaRepository.getIdByEmail(data.email());
        Long id = ((Number) pessoaData.get("id")).longValue();
        String sql = "INSERT INTO Aluno (fk_Pessoa_id_pessoa, periodo) VALUES (?, ?)";
        jdbcTemplate.update(sql, id, data.periodo());
    }
    public void cadastrarProfessor(CadastroDTO data) {
        Map<String, Object> pessoaData = pessoaRepository.getIdByEmail(data.email());
        Long id = ((Number) pessoaData.get("id")).longValue();
        String sql = "INSERT INTO Professor (fk_Pessoa_id_pessoa, especializacao) \n" +
                "VALUES (?, ?)";
        jdbcTemplate.update(sql, id,data.especializacao());
    }

    public void cadastrarSecretaria(CadastroDTO data) {
        Map<String, Object> pessoaData = pessoaRepository.getIdByEmail(data.email());
        Long id = ((Number) pessoaData.get("id")).longValue();
        String sql = "INSERT INTO Secretaria (fk_Pessoa_id_pessoa, fk_Secretaria_fk_Pessoa_id_pessoa)\n" +
                "VALUES (?, ?),";
        jdbcTemplate.update(sql, id,data.idSecretaria());
    }
}

