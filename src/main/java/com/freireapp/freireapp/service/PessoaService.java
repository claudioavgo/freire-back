package com.freireapp.freireapp.service;

import com.freireapp.freireapp.pessoa.Pessoa;
import com.freireapp.freireapp.repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Service
public class PessoaService {

    @Autowired
    private PessoaRepository pessoaRepository;

    public List<Map<String, Object>> listarTodasPessoas() {
        List<Map<String, Object>> todasPessoas = pessoaRepository.getAllPessoas();

        for (Map<String, Object> pessoa : todasPessoas) {
            int pessoaId = (int) pessoa.getOrDefault("id_pessoa", -1);
            if (pessoaId == -1) {
                pessoa.put("tipo", "Não especificado");
                continue;
            }

            boolean isAluno = pessoaRepository.eAluno(pessoaId);
            boolean isProfessor = pessoaRepository.eProfessor(pessoaId);
            boolean isSecretaria = pessoaRepository.eSecretaria(pessoaId);

            String tipoPessoa = "Não especificado";
            if (isAluno) {
                tipoPessoa = "Aluno";
            } else if (isProfessor) {
                tipoPessoa = "Professor";
            } else if (isSecretaria) {
                tipoPessoa = "Secretaria";
            }

            pessoa.put("tipo", tipoPessoa);

            System.out.println("Pessoa ID: " + pessoaId + ", Tipo: " + tipoPessoa);
        }

        return todasPessoas;
    }

    public Map<String, Object> buscarPessoaPorId(Long id) {
        return pessoaRepository.getPessoaById(id);
    }

    public Pessoa autenticar(String email, String senha){
        Pessoa pessoa = pessoaRepository.getPessoaEmail(email);

        if (pessoa.getSenha().equals(senha)) {
            if(pessoaRepository.eAluno(pessoa.getIdPessoa())) {
                pessoa.setTipo("aluno");
            } else {
                pessoa.setTipo("professor");
            }
            return pessoa;
        }

        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário ou senha incorreto.");
    }
}