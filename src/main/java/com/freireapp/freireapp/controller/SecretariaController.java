package com.freireapp.freireapp.controller;

import com.freireapp.freireapp.dto.CadastroDTO;
import com.freireapp.freireapp.service.PessoaService;
import com.freireapp.freireapp.service.SecretariaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/secretaria")
public class SecretariaController {

    @Autowired
    private SecretariaService secretariaService;

    @PostMapping("/cadastrar")
    public ResponseEntity cadastrarPessoa(@RequestBody CadastroDTO data) {
        return ResponseEntity.status(200).body(secretariaService.cadastro(data));
    }
}
