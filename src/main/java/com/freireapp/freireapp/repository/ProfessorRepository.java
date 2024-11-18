package com.freireapp.freireapp.repository;

import com.freireapp.freireapp.dto.NotasAvaliacaoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;

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
                "    p_aluno.nome AS aluno, " +
                "    GROUP_CONCAT(ra.nota SEPARATOR ', ') AS notas " + // Assuming 'nota' is the column for scores
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
            Object notasObj = row.get("notas");
            if (notasObj != null && notasObj instanceof String) {
                String notas = (String) notasObj;
                String[] notasArray = notas.split(",\\s*"); // Split by comma and optional whitespace
                List<Double> notasNumericas = new ArrayList<>();
                for (String nota : notasArray) {
                    try {
                        notasNumericas.add(Double.valueOf(nota));
                    } catch (NumberFormatException e) {
                        // Handle parsing error if needed, using 0.0 as a default value
                        notasNumericas.add(0.0);
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

    public void inserirResultadoAvaliacao(Long id,NotasAvaliacaoDTO data) {
        String sql = "INSERT INTO ResultadoAvaliacao " +
                "(fk_Aluno_fk_Pessoa_id_pessoa, fk_Professor_fk_Pessoa_id_pessoa, fk_Avaliacao_id_avaliacao, nota, feedback) " +
                "VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, data.idAluno(), data.idProfessor(), id, data.nota(), data.feedback());
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

    public List<Map<String, Object>> getAlunosPorDisciplina(Long idDisciplina) {
        String sql = "SELECT p.nome AS nome_aluno, a.fk_Pessoa_id_pessoa AS id_aluno, a.periodo AS periodo_aluno, p.email AS email_aluno, " +
                "ra.nota AS nota_aluno " +
                "FROM Matriculado m " +
                "JOIN Aluno a ON a.fk_Pessoa_id_pessoa = m.fk_Aluno_fk_Pessoa_id_pessoa " +
                "JOIN Pessoa p ON p.id_pessoa = a.fk_Pessoa_id_pessoa " +
                "LEFT JOIN ResultadoAvaliacao ra ON ra.fk_Aluno_fk_Pessoa_id_pessoa = a.fk_Pessoa_id_pessoa AND ra.fk_Avaliacao_id_avaliacao IN " +
                "(SELECT id_avaliacao FROM Avaliacao WHERE fk_Disciplina_id_disciplina = m.fk_Disciplina_id_disciplina) " +
                "WHERE m.fk_Disciplina_id_disciplina = ? " +
                "ORDER BY a.fk_Pessoa_id_pessoa";

        return jdbcTemplate.queryForList(sql, idDisciplina);
    }
    public List<Map<String, Object>> listarAvaliacao(Long id) {
        String sql = "SELECT \n" +
                "    d.nome AS disciplina,\n" +
                "    a.id_avaliacao,\n" +
                "    a.descricao,\n" +
                "    a.data \n" +
                "FROM \n" +
                "    Avaliacao a\n" +
                "JOIN \n" +
                "    Disciplina d ON a.fk_Disciplina_id_disciplina = d.id_disciplina\n" +
                "WHERE d.id_disciplina = ?\n" +
                "ORDER BY \n" +
                "    d.nome, a.data";
        return jdbcTemplate.queryForList(sql, id);
    }

    public List<Map<String, Object>> getResultadosPorAvaliacao(Long idAvaliacao) {
        String sql = "SELECT " +
                "    p.nome AS nome_aluno, " +
                "    a.fk_Pessoa_id_pessoa AS id_aluno, " +
                "    COALESCE(ra.nota, -1) AS nota, " + // Retorna -1 quando não há nota
                "    COALESCE(ra.feedback, '') AS feedback " +
                "FROM " +
                "    Aluno a " +
                "JOIN " +
                "    Pessoa p ON p.id_pessoa = a.fk_Pessoa_id_pessoa " +
                "LEFT JOIN " +
                "    Matriculado m ON m.fk_Aluno_fk_Pessoa_id_pessoa = a.fk_Pessoa_id_pessoa " +
                "LEFT JOIN " +
                "    ResultadoAvaliacao ra ON ra.fk_Aluno_fk_Pessoa_id_pessoa = a.fk_Pessoa_id_pessoa " +
                "    AND ra.fk_Avaliacao_id_avaliacao = ? " +
                "WHERE " +
                "    m.fk_Disciplina_id_disciplina = (SELECT fk_Disciplina_id_disciplina FROM Avaliacao WHERE id_avaliacao = ?)";

        return jdbcTemplate.queryForList(sql, idAvaliacao, idAvaliacao);
    }

    public Boolean avaliacaoExiste(Long id) {
        String sql = "SELECT COUNT(*) FROM Avaliacao a WHERE a.id_avaliacao = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    public void deletarAvaliacao(Long id) {
        String sql = "DELETE FROM Avaliacao WHERE id_avaliacao = ?";
        jdbcTemplate.update(sql, id);
    }

    public void atualizarAvaliacao(Long idAvaliacao, String descricao, LocalDate data) {
        String sql = "UPDATE Avaliacao " +
                "SET descricao = ?, data = ? " +
                "WHERE id_avaliacao = ?";

        int rowsAffected = jdbcTemplate.update(sql, descricao, data, idAvaliacao);

        if (rowsAffected == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Avaliação não encontrada.");
        }
    }

    public List<Map<String, Object>> getAulasdoDia(Long idProfessor) {
        String sql = "SELECT " +
                "    a.id_aula, " +
                "    a.dia_semana, " +
                "    a.hora_inicio, " +
                "    a.hora_fim, " +
                "    a.sala, " +
                "    d.nome AS disciplina, " +
                "    EXISTS ( " +
                "        SELECT 1 FROM Avaliacao av " +
                "        WHERE av.fk_Disciplina_id_disciplina = d.id_disciplina " +
                "          AND av.data = CURRENT_DATE " +
                "    ) AS is_prova " +
                "FROM " +
                "    Aula a " +
                "JOIN " +
                "    Disciplina d ON a.fk_Disciplina_id_disciplina = d.id_disciplina " +
                "WHERE " +
                "    a.fk_Professor_fk_Pessoa_id_pessoa = ? " +
                "    AND a.dia_semana = ?";

        String diaSemanaAtual = LocalDate.now().getDayOfWeek()
                .getDisplayName(TextStyle.FULL, new Locale("pt", "BR"));

        return jdbcTemplate.queryForList(sql, idProfessor, diaSemanaAtual);
    }

    public int contarProvasCorrigidas(Long idProfessor) {
        String sql = "SELECT COUNT(*) AS total_corrigidas " +
                "FROM ResultadoAvaliacao ra " +
                "JOIN Avaliacao a ON ra.fk_Avaliacao_id_avaliacao = a.id_avaliacao " +
                "JOIN Disciplina d ON a.fk_Disciplina_id_disciplina = d.id_disciplina " +
                "WHERE d.fk_Professor_fk_Pessoa_id_pessoa = ? " +
                "  AND ra.nota IS NOT NULL"; // Filtra somente provas corrigidas

        return jdbcTemplate.queryForObject(sql, Integer.class, idProfessor);
    }

    public Map<String, Object> contarAulasMinistradas(Long idProfessor) {
        String sql = "SELECT COUNT(DISTINCT p.data) AS total_aulas " +
                "FROM Presenca p " +
                "WHERE p.fk_Professor_fk_Pessoa_id_pessoa = ?";
        return jdbcTemplate.queryForMap(sql, idProfessor);
    }



}
