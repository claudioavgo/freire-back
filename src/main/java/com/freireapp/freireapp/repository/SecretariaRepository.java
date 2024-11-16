package com.freireapp.freireapp.repository;

import com.freireapp.freireapp.dto.CadastroDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SecretariaRepository {
    private final JdbcTemplate jdbcTemplate;

    public SecretariaRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int cadastrarPessoa(CadastroDTO data) {
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
        if (data.tipo() == 0) {
            String sql1 = "INSERT INTO Aluno (fk_Pessoa_id_pessoa, periodo) \n" +
                    "VALUES  (?, ?, ?)";
            return jdbcTemplate.update(sql1);
        }
        return 0;
    }
}

