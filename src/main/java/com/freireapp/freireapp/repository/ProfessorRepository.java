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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
                "    GROUP_CONCAT(DISTINCT ra.nota ORDER BY ra.nota SEPARATOR ', ') AS notas, " +
                "    COUNT(DISTINCT CASE WHEN pr.status = 'F' THEN pr.id_presenca END) AS faltas " +
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
                    notasNumericas.add(Double.valueOf(nota));
                }
                row.put("notas", notasNumericas);
            } else {
                row.put("notas", new ArrayList<>());
            }
        }
        return results;
    }

    public void inserirAvaliacao(Long idDisciplina, String descricao, LocalDate data) {
        String sql = "INSERT INTO Avaliacao (descricao, data, fk_Disciplina_id_disciplina) VALUES (?, ?, ?)";

        int rowsAffected = jdbcTemplate.update(sql, descricao, data, idDisciplina);

        if (rowsAffected == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erro");
        }
    }

}