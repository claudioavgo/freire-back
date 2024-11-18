package com.freireapp.freireapp.repository;

import com.freireapp.freireapp.pessoa.Pessoa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
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

    public boolean eProfessor(int id) {
        try {
            String sql = "SELECT * FROM Professor WHERE fk_Pessoa_id_pessoa = ?";
            return !jdbcTemplate.queryForMap(sql,id).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean eSecretaria(int id) {
        try {
            String sql = "SELECT * FROM Secretaria WHERE fk_Pessoa_id_pessoa = ?";
            return !jdbcTemplate.queryForMap(sql, id).isEmpty();
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

    public Map<String, Object> getIdByEmail(String email) {
        String sql = "SELECT p.id_pessoa FROM Pessoa p WHERE p.email = ?";
        return jdbcTemplate.queryForMap(sql,email);
    }

    public List<Map<String, Object>> getAulasDoDia(Long idPessoa) {
        String sql = "SELECT a.dia_semana, " +
                "       a.hora_inicio, " +
                "       a.hora_fim, " +
                "       a.sala, " +
                "       d.nome AS disciplina, " +
                "       p.nome AS nome_professor, " +
                "       CASE " +
                "           WHEN EXISTS ( " +
                "               SELECT 1 " +
                "               FROM Avaliacao av " +
                "               WHERE av.fk_Disciplina_id_disciplina = d.id_disciplina " +
                "                 AND av.data = CURRENT_DATE " +
                "           ) THEN true " +
                "           ELSE false " +
                "       END AS is_prova " +
                "FROM Aula a " +
                "JOIN Disciplina d ON a.fk_Disciplina_id_disciplina = d.id_disciplina " +
                "JOIN Professor pr ON d.fk_Professor_fk_Pessoa_id_pessoa = pr.fk_Pessoa_id_pessoa " +
                "JOIN Pessoa p ON pr.fk_Pessoa_id_pessoa = p.id_pessoa " +
                "LEFT JOIN Matriculado m ON m.fk_Disciplina_id_disciplina = d.id_disciplina " +
                "WHERE (a.fk_Professor_fk_Pessoa_id_pessoa = ? " +
                "       OR m.fk_Aluno_fk_Pessoa_id_pessoa = ?) " +
                "  AND a.dia_semana = ?";

        String diaSemanaAtual = LocalDate.now().getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("pt", "BR"));
        String diaSemana = diaSemanaAtual.substring(0, 1).toUpperCase() + diaSemanaAtual.substring(1);

        List<Map<String, Object>> aulas = jdbcTemplate.queryForList(sql, idPessoa, idPessoa, diaSemana);

        // Garantindo que 'is_prova' seja um booleano
        for (Map<String, Object> aula : aulas) {
            Object isProva = aula.get("is_prova");
            if (isProva instanceof Number) {
                aula.put("is_prova", ((Number) isProva).intValue() == 1);
            }
        }

        return aulas;
    }


}
