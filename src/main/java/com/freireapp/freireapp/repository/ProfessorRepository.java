package com.freireapp.freireapp.repository;

import com.freireapp.freireapp.dto.NotasAvaliacaoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class ProfessorRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Map<String, Object> getAlunos(Long idProfessor) {
        String sql = "SELECT COUNT(DISTINCT m.fk_Aluno_fk_Pessoa_id_pessoa) AS quantidade_alunos " +
                "FROM Disciplina d " +
                "JOIN Matriculado m ON m.fk_Disciplina_id_disciplina = d.id_disciplina " +
                "WHERE d.fk_Professor_fk_Pessoa_id_pessoa = ?";
        return jdbcTemplate.queryForMap(sql, idProfessor);
    }

    public List<Map<String, Object>> getAlunosInformacoes(Long idProfessor) {
        String sql = "SELECT " +
                "    d.nome AS disciplina, " +
                "    p_aluno.nome AS aluno " + // Removed the trailing comma
                "FROM " +
                "    Disciplina d " +
                "JOIN " +
                "    Matriculado m ON m.fk_Disciplina_id_disciplina = d.id_disciplina " +
                "JOIN " +
                "    Aluno al ON al.fk_Pessoa_id_pessoa = m.fk_Aluno_fk_Pessoa_id_pessoa " +
                "JOIN " +
                "    Pessoa p_aluno ON p_aluno.id_pessoa = al.fk_Pessoa_id_pessoa " +
                "LEFT JOIN " +
                "    ResultadoAvaliacao ra ON ra.fk_Aluno_fk_Pessoa_id_pessoa = al.fk_Pessoa_id_pessoa " +
                "                         AND ra.fk_Avaliacao_id_avaliacao IN ( " +
                "                             SELECT id_avaliacao " +
                "                             FROM Avaliacao " +
                "                             WHERE Avaliacao.fk_Disciplina_id_disciplina = d.id_disciplina " +
                "                         ) " +
                "LEFT JOIN " +
                "    Presenca pr ON pr.fk_Aluno_fk_Pessoa_id_pessoa = al.fk_Pessoa_id_pessoa " +
                "              AND pr.fk_Professor_fk_Pessoa_id_pessoa = d.fk_Professor_fk_Pessoa_id_pessoa " +
                "WHERE " +
                "    d.fk_Professor_fk_Pessoa_id_pessoa = ? " +
                "GROUP BY " +
                "    d.nome, p_aluno.nome";

        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, idProfessor);

        for (Map<String, Object> row : results) {
            String notas = (String) row.get("notas");
            if (notas != null) {
                String[] notasArray = notas.split(", ");
                List<Double> notasNumericas = new ArrayList<>();
                for (String nota : notasArray) {
                    try {
                        notasNumericas.add(Double.valueOf(nota));
                    } catch (NumberFormatException e) {
                        // Handle parsing error if needed
                        notasNumericas.add(0.0); // Add a default value or handle accordingly
                    }
                }
                row.put("notas", notasNumericas);
            } else {
                row.put("notas", new ArrayList<>());
            }
        }
        return results;
    }

    public void criarAvaliacao(Long idDisciplina, String descricao, LocalDate data) {
        String sql = "INSERT INTO Avaliacao (descricao, data, fk_Disciplina_id_disciplina) VALUES (?, ?, ?)";
        int rowsAffected = jdbcTemplate.update(sql, descricao, data, idDisciplina);

        if (rowsAffected == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erro ao criar avaliação");
        }
    }

    public void inserirResultadoAvaliacao(NotasAvaliacaoDTO data) {
        String sql = "INSERT INTO ResultadoAvaliacao " +
                "(fk_Aluno_fk_Pessoa_id_pessoa, fk_Professor_fk_Pessoa_id_pessoa, fk_Avaliacao_id_avaliacao, nota, feedback) " +
                "VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, data.idAluno(), data.idProfessor(), data.idAvaliacao(), data.nota(), data.feedback());
    }

    public List<Map<String, Object>> listarDisciplinas(Long id) {
        String sql = "SELECT d.id_disciplina ,d.nome, COUNT(m.fk_Aluno_fk_Pessoa_id_pessoa) AS num_alunos\n" +
                "FROM Professor p\n" +
                "JOIN Disciplina d ON d.fk_Professor_fk_Pessoa_id_pessoa = p.fk_Pessoa_id_pessoa\n" +
                "LEFT JOIN Matriculado m ON m.fk_Disciplina_id_disciplina = d.id_disciplina\n" +
                "WHERE p.fk_Pessoa_id_pessoa = ?\n" +
                "GROUP BY d.id_disciplina, d.nome";
        return jdbcTemplate.queryForList(sql, id);
    }
}
