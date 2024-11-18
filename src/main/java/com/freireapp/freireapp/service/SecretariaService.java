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

    public ResponseEntity cadastro(CadastroDTO data) {
        try {
            Map<String, Object> emailExistente = pessoaRepository.getPessoaByEmail(data.email());
            if (emailExistente != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("E-mail já está cadastrado.");
            }

            if (data.telefone1() != null) {
                Map<String, Object> telefoneExistente = pessoaRepository.getPessoaByTelefone(data.telefone1());
                if (telefoneExistente != null) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("Telefone já está cadastrado.");
                }
            }

            secretariaRepository.cadastrarPessoa(data);
            Long idPessoa = secretariaRepository.obterIdPessoaPorEmail(data.email());

            if (data.tipo() == 0) {
                secretariaRepository.cadastrarAluno(data.periodo(), idPessoa);
            } else if (data.tipo() == 1) {
                secretariaRepository.cadastrarProfessor(data, idPessoa);
            } else if (data.tipo() == 2) {
                secretariaRepository.cadastrarSecretaria(data, idPessoa);
            }

            return ResponseEntity.status(200).body("Usuário cadastrado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao cadastrar usuário: " + e.getMessage());
        }
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