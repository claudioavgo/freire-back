package com.freireapp.freireapp.service;

import com.freireapp.freireapp.dto.CadastroDTO;
import com.freireapp.freireapp.pessoa.PessoaRepository;
import com.freireapp.freireapp.secretaria.SecretariaRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
        return ResponseEntity.status(200).body("Usuário cadastrado com sucesso!");
    }
}
