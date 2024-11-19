package com.freireapp.freireapp.controller;

import com.freireapp.freireapp.dto.CadastroDTO;
import com.freireapp.freireapp.service.PessoaService;
import com.freireapp.freireapp.service.SecretariaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/secretaria")
@CrossOrigin(origins = "*")
public class SecretariaController {

    @Autowired
    private SecretariaService secretariaService;

    @PostMapping("/cadastrar")
    public ResponseEntity cadastrarPessoa(@RequestBody CadastroDTO data) {
        return ResponseEntity.status(201).body(secretariaService.cadastro(data));
    };

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity deletarPessoa(@PathVariable Long id){
        return secretariaService.deletarPessoa(id);
    };
}
