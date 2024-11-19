package com.freireapp.freireapp.repository;

import com.freireapp.freireapp.PasswordUtils;
import com.freireapp.freireapp.dto.AlunoDisciplinaDTO;
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
        String hashedPassword = PasswordUtils.hashPassword(data.senha()); // Hash the password
        String sql = "INSERT INTO Pessoa (nome, rua, numero, cidade, telefone_1, telefone_2, email, senha, data_nascimento) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                data.nome(),
                data.rua(),
                data.numero(),
                data.cidade(),
                data.telefone1(),
                data.telefone2(),
                data.email(),
                hashedPassword,
                data.dataNascimento()
        );
    }

    public Long obterIdPessoaPorEmail(String email) {
        String sql = "SELECT id_pessoa FROM Pessoa WHERE email = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, email);
    }

    public void cadastrarAluno(int periodo, Long idPessoa) {
        String sql = "INSERT INTO Aluno (fk_Pessoa_id_pessoa, periodo) VALUES (?, ?)";
        jdbcTemplate.update(sql, idPessoa, periodo);
    }

    public void cadastrarProfessor(CadastroDTO data, Long idPessoa) {
        String sql = "INSERT INTO Professor (fk_Pessoa_id_pessoa, especializacao) VALUES (?, ?)";
        jdbcTemplate.update(sql, idPessoa, data.especializacao());
    }

    public void cadastrarSecretaria(CadastroDTO data, Long idPessoa) {
        String sql = "INSERT INTO Secretaria (fk_Pessoa_id_pessoa, fk_Secretaria_fk_Pessoa_id_pessoa) VALUES (?, ?)";
        jdbcTemplate.update(sql, idPessoa, data.idSecretaria());
    }


    public Boolean pessoaExiste(Long id) {
        String sql = "SELECT COUNT(*) FROM Pessoa WHERE id_pessoa = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    public void deletarPessoa(Long id) {
        String sql = "DELETE FROM Pessoa WHERE id_pessoa = ?";
        jdbcTemplate.update(sql, id);
    }

    public void cadastrarDisciplina(AlunoDisciplinaDTO data) {
        String sql = "INSERT INTO Matriculado (fk_Disciplina_id_disciplina, fk_Aluno_fk_Pessoa_id_pessoa) VALUES (?, ?)";
        jdbcTemplate.update(sql, data.idDisciplina(), data.idAluno());
    }

    public boolean alunoJaMatriculado(int idAluno, int idDisciplina) {
        String sql = "SELECT COUNT(*) FROM Matriculado WHERE fk_Disciplina_id_disciplina = ? AND fk_Aluno_fk_Pessoa_id_pessoa = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, idDisciplina, idAluno);
        return count != null && count > 0;
    }



}
