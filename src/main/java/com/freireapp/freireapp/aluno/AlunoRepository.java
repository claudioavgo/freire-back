package com.freireapp.freireapp.aluno;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class AlunoRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> getQuantidadeFaltas(Long id) {
        String sql = "SELECT p.nome AS professor, pf.especializacao AS cadeira, " +
                "SUM(CASE WHEN pr.status = 0 THEN 1 ELSE 0 END) AS faltas " +
                "FROM Aula a " +
                "JOIN Professor pf ON a.fk_Professor_fk_Pessoa_id_pessoa = pf.fk_Pessoa_id_pessoa " +
                "JOIN Pessoa p ON pf.fk_Pessoa_id_pessoa = p.id_pessoa " +
                "LEFT JOIN Presenca pr ON pr.fk_Professor_fk_Pessoa_id_pessoa = pf.fk_Pessoa_id_pessoa " +
                "AND pr.fk_Aluno_fk_Pessoa_id_pessoa = ? " +
                "WHERE a.fk_Aluno_fk_Pessoa_id_pessoa = ? " +
                "GROUP BY pf.fk_Pessoa_id_pessoa, pf.especializacao, p.nome";
        return jdbcTemplate.queryForList(sql, id, id);
    }

    public List<Map<String, Object>> getResultadoAvaliacao(Long id) {
        String sql = "select p2.especializacao as cadeira, a2.descricao, a2.nota, rp.`data`\n" +
                "from Pessoa p join Aluno a on a.fk_Pessoa_id_pessoa = p.id_pessoa\n" +
                "join RealizaAvaliacao rp on rp.fk_Aluno_fk_Pessoa_id_pessoa = p.id_pessoa\n" +
                "join Avaliacao a2 on a2.id_avaliacao = rp.fk_Avaliacao_id_avaliacao\n" +
                "join Professor p2 on p2.fk_Pessoa_id_pessoa = rp.fk_Professor_fk_Pessoa_id_pessoa\n" +
                "where p.id_pessoa = ?";
        return jdbcTemplate.queryForList(sql,id);
    }

    public List<Map<String,Object>> getTodasCadeiras(Long id) {
        String sql = "SELECT a2.dia_semana AS dia_aula, a2.hora_inicio, a2.hora_fim, a2.sala, p3.especializacao AS cadeira, p_prof.nome AS nome_professor, p_prof.id_pessoa AS id_professor\n" +
                "FROM Pessoa p \n" +
                "JOIN Aluno a ON a.fk_Pessoa_id_pessoa = p.id_pessoa\n" +
                "JOIN Aula a2 ON a.fk_Pessoa_id_pessoa = a2.fk_Aluno_fk_Pessoa_id_pessoa\n" +
                "JOIN Professor p3 ON p3.fk_Pessoa_id_pessoa = a2.fk_Professor_fk_Pessoa_id_pessoa\n" +
                "JOIN Pessoa p_prof ON p_prof.id_pessoa = p3.fk_Pessoa_id_pessoa\n" +
                "WHERE p.id_pessoa = ?";
        return jdbcTemplate.queryForList(sql,id);
    }

    public List<Map<String,Object>> getAulasDoDia(Long id, String dia) {
        String sql = "SELECT a2.dia_semana, a2.hora_inicio, a2.hora_fim, a2.sala, p2.especializacao as cadeira, p2.fk_Pessoa_id_pessoa as id_professor\n" +
                "from Pessoa p join Aluno a on a.fk_Pessoa_id_pessoa = p.id_pessoa\n" +
                "join Aula a2 ON a.fk_Pessoa_id_pessoa = a2.fk_Aluno_fk_Pessoa_id_pessoa\n" +
                "join Professor p2 on a2.fk_Professor_fk_Pessoa_id_pessoa = p2.fk_Pessoa_id_pessoa\n" +
                "Where p.id_pessoa = ? and dia_semana = ?";
        return jdbcTemplate.queryForList(sql,id,dia);
    }
}
