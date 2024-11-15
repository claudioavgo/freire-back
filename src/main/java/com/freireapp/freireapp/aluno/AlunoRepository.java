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

    public List<Map<String,Object>> getQuantidadeFaltas(Long id) {
        String sql = "SELECT \n" +
                "    p.nome as professor, pf.especializacao AS cadeira,\n" +
                "    COUNT(pr.id_presenca) AS faltas\n" +
                "FROM \n" +
                "    presenca pr\n" +
                "INNER JOIN professor pf ON pr.fk_Professor_fk_Pessoa_id_pessoa = pf.fk_Pessoa_id_pessoa\n" +
                "INNER JOIN pessoa p ON pf.fk_Pessoa_id_pessoa = p.id_pessoa\n" +
                "WHERE \n" +
                "    pr.fk_Aluno_fk_Pessoa_id_pessoa = ? \n" +
                "    AND pr.status = 0\n" +
                "GROUP BY \n" +
                "    pf.fk_Pessoa_id_pessoa";
        return jdbcTemplate.queryForList(sql, id);
    }

    public List<Map<String, Object>> getResultadoAvaliacao(Long id) {
        String sql = "select p2.especializacao as cadeira, a2.descricao, a2.nota, rp.`data`\n" +
                "from pessoa p join aluno a on a.fk_Pessoa_id_pessoa = p.id_pessoa\n" +
                "join realiza_aluno_professor_avaliacao rp on rp.fk_Aluno_fk_Pessoa_id_pessoa = p.id_pessoa\n" +
                "join avaliacao a2 on a2.id_avaliacao = rp.fk_Avaliacao_id_avaliacao\n" +
                "join professor p2 on p2.fk_Pessoa_id_pessoa = rp.fk_Professor_fk_Pessoa_id_pessoa\n" +
                "where p.id_pessoa = ?";
        return jdbcTemplate.queryForList(sql,id);
    }
}
