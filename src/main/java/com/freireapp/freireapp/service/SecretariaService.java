package com.freireapp.freireapp.service;

import com.freireapp.freireapp.dto.CadastroDTO;
import com.freireapp.freireapp.repository.PessoaRepository;
import com.freireapp.freireapp.repository.SecretariaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SecretariaService {

    @Autowired
    private SecretariaRepository secretariaRepository;
    @Autowired
    private PessoaRepository pessoaRepository;

    public ResponseEntity cadastro(CadastroDTO data){
        Map<String, Object> emailRepetido = pessoaRepository.getPessoaByEmail(data.email());
        if(emailRepetido != null) {
            return ResponseEntity.status(409).body("Já existe um usuário cadastrado com este e-mail.");
        }
        if(data.telefone1()!=null) {
            Map<String, Object> telefoneRepetido = pessoaRepository.getPessoaByTelefone(data.telefone1());
            if(telefoneRepetido != null) {
                return ResponseEntity.status(409).body("Já existe um usuário cadastrado com este telefone.");
            }
        }
        secretariaRepository.cadastrarPessoa(data);
        if(data.tipo() == 0) {
            secretariaRepository.cadastrarAluno(data);
        } else if (data.tipo() == 1) {
            secretariaRepository.cadastrarProfessor(data);
        } else if (data.tipo() == 2 ) {
            secretariaRepository.cadastrarSecretaria(data);
        }
        return ResponseEntity.status(200).body("Usuário cadastrado com sucesso!");
    }

    public ResponseEntity<String> deletarPessoa(Long id) {
        if (!secretariaRepository.pessoaExiste(id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Pessoa não encontrada.");
        }
        try {
            secretariaRepository.deletarPessoa(id);
            return ResponseEntity.status(HttpStatus.OK).body("Pessoa removida com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao remover a pessoa.");
        }
    }
}