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
        return pessoaRepository.getAllPessoas();
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