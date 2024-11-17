package com.freireapp.freireapp.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class AlunoRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> getQuantidadeFaltas(Long idAluno, Long idDisciplina) {
        String sql = "SELECT d.nome AS disciplina, SUM(CASE WHEN pr.status = 0 THEN 1 ELSE 0 END) AS faltas\n" +
                "FROM Presenca pr\n" +
                "JOIN Aula a ON pr.fk_Professor_fk_Pessoa_id_pessoa = a.fk_Professor_fk_Pessoa_id_pessoa\n" +
                "JOIN Disciplina d ON a.fk_Disciplina_id_disciplina = d.id_disciplina\n" +
                "WHERE pr.fk_Aluno_fk_Pessoa_id_pessoa = ? AND d.id_disciplina = ?";
        return jdbcTemplate.queryForList(sql, idAluno, idDisciplina);
    }

    public List<Map<String, Object>> getTodasProvas(Long id) {
        String sql = "SELECT d.id_disciplina ,d.nome, a.descricao, ra.nota,ra.feedback, a.`data` \n" +
                "FROM ResultadoAvaliacao ra\n" +
                "JOIN Avaliacao a ON ra.fk_Avaliacao_id_avaliacao = a.id_avaliacao JOIN Disciplina d on d.id_disciplina = a.fk_Disciplina_id_disciplina \n" +
                "WHERE ra.fk_Aluno_fk_Pessoa_id_pessoa = ?";
        return jdbcTemplate.queryForList(sql,id);
    }

    public List<Map<String, Object>> getResultadoAvaliacao(Long idAluno, Long idDisciplina) {
        String sql = "SELECT d.id_disciplina ,d.nome, a.descricao, ra.nota,ra.feedback, a.`data` \n" +
                "FROM ResultadoAvaliacao ra\n" +
                "JOIN Avaliacao a ON ra.fk_Avaliacao_id_avaliacao = a.id_avaliacao JOIN Disciplina d on d.id_disciplina = a.fk_Disciplina_id_disciplina \n" +
                "WHERE ra.fk_Aluno_fk_Pessoa_id_pessoa = ? and d.id_disciplina = ?";
        return jdbcTemplate.queryForList(sql,idAluno, idDisciplina);
    }

    public List<Map<String, Object>> getTodasDisciplinas(Long idAluno) {
        String sql = "SELECT DISTINCT d.id_disciplina, d.nome AS disciplina, pe.nome AS professor\n" +
                "FROM Matriculado m\n" +
                "JOIN Disciplina d ON m.fk_Disciplina_id_disciplina = d.id_disciplina\n" +
                "JOIN Aula a ON a.fk_Disciplina_id_disciplina = d.id_disciplina\n" +
                "JOIN Professor p ON p.fk_Pessoa_id_pessoa = d.fk_Professor_fk_Pessoa_id_pessoa\n" +
                "JOIN Pessoa pe ON pe.id_pessoa = p.fk_Pessoa_id_pessoa\n" +
                "WHERE m.fk_Aluno_fk_Pessoa_id_pessoa = ?";
        return jdbcTemplate.queryForList(sql, idAluno);
    }


    public List<Map<String, Object>> getAulasDoDia(Long idAluno, String diaSemana) {
        String sql = "SELECT a.dia_semana, " +
                "       a.hora_inicio, " +
                "       a.hora_fim, " +
                "       a.sala, " +
                "       d.nome AS disciplina, " +
                "       p.nome AS professor, " +
                "       p.id_pessoa AS id_professor " +
                "FROM Matriculado m " +
                "JOIN Disciplina d ON m.fk_Disciplina_id_disciplina = d.id_disciplina " +
                "JOIN Aula a ON d.id_disciplina = a.fk_Disciplina_id_disciplina " +
                "JOIN Professor pr ON d.fk_Professor_fk_Pessoa_id_pessoa = pr.fk_Pessoa_id_pessoa " +
                "JOIN Pessoa p ON pr.fk_Pessoa_id_pessoa = p.id_pessoa " +
                "WHERE m.fk_Aluno_fk_Pessoa_id_pessoa = ?" +
                "  AND a.dia_semana = ?";
        return jdbcTemplate.queryForList(sql, idAluno, diaSemana);
    }


    public List<Map<String, Object>> getAulas(Long id) {
        String sql = "SELECT a2.dia_semana, a2.hora_inicio, a2.hora_fim, a2.sala, p2.especializacao as cadeira, p2.fk_Pessoa_id_pessoa as id_professor " +
                "FROM Pessoa p " +
                "JOIN Aluno a ON a.fk_Pessoa_id_pessoa = p.id_pessoa " +
                "JOIN Aula a2 ON a.fk_Pessoa_id_pessoa = a2.fk_Aluno_fk_Pessoa_id_pessoa " +
                "JOIN Professor p2 ON a2.fk_Professor_fk_Pessoa_id_pessoa = p2.fk_Pessoa_id_pessoa " +
                "WHERE p.id_pessoa = ?";
        return jdbcTemplate.queryForList(sql, id);
    }

    public Map<String, Object> getStreak(Long idAluno) {
        String sql = "WITH PresencasOrdenadas AS ( " +
                "    SELECT pr.data, " +
                "           pr.status, " +
                "           ROW_NUMBER() OVER (PARTITION BY pr.fk_Aluno_fk_Pessoa_id_pessoa ORDER BY pr.data) AS rn " +
                "    FROM Presenca pr " +
                "    WHERE pr.fk_Aluno_fk_Pessoa_id_pessoa = ? " +
                "), " +
                "StreakAtual AS ( " +
                "    SELECT COUNT(*) AS streak_atual " +
                "    FROM ( " +
                "        SELECT *, " +
                "               SUM(CASE WHEN status = 0 THEN 1 ELSE 0 END) OVER (ORDER BY data) AS grupo " +
                "        FROM PresencasOrdenadas " +
                "    ) SubPresencas " +
                "    WHERE status = 1 " +
                "      AND grupo = ( " +
                "          SELECT MAX(grupo) " +
                "          FROM ( " +
                "              SELECT SUM(CASE WHEN status = 0 THEN 1 ELSE 0 END) OVER (ORDER BY data) AS grupo " +
                "              FROM PresencasOrdenadas " +
                "          ) Temp " +
                "      ) " +
                ") " +
                "SELECT streak_atual " +
                "FROM StreakAtual";
        return jdbcTemplate.queryForMap(sql, idAluno);
    }

    public List<Map<String, Object>> getPagamentos(Long idAluno) {
        String sql = "SELECT pg.id_pagamento, pg.data_pagamento, pg.valor, pg.status \n" +
                "FROM Pagamento pg\n" +
                "JOIN RealizaPagamento rp ON pg.id_pagamento = rp.fk_Pagamento_id_pagamento\n" +
                "JOIN Aluno a ON rp.fk_Aluno_fk_Pessoa_id_pessoa = a.fk_Pessoa_id_pessoa\n" +
                "JOIN Pessoa p ON a.fk_Pessoa_id_pessoa = p.id_pessoa\n" +
                "WHERE p.id_pessoa = ?";
        return jdbcTemplate.queryForList(sql, idAluno);
    }

    public Map<String, Object> getMediaTotal(Long idAluno) {
        String sql = "SELECT AVG(nota) AS media_rendimento\n" +
                "FROM ResultadoAvaliacao\n" +
                "WHERE fk_Aluno_fk_Pessoa_id_pessoa = ?";
        return jdbcTemplate.queryForMap(sql, idAluno);
    }





}
