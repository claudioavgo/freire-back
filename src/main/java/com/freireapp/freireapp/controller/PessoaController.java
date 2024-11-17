package com.freireapp.freireapp.controller;

import com.freireapp.freireapp.dto.LoginDTO;
import com.freireapp.freireapp.service.PessoaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pessoas")
@CrossOrigin(origins = "*")
public class PessoaController {

    @Autowired
    private PessoaService pessoaService;

    /*@GetMapping
    public List<Map<String, Object>> listarPessoas() {
        return pessoaService.listarTodasPessoas();
    }

    @GetMapping("/{id}")
    public Map<String, Object> buscarPessoa(@PathVariable Long id) {
        return pessoaService.buscarPessoaPorId(id);
    }

    @PostMapping("/autenticar")
    public ResponseEntity autenticarPessoa(@RequestBody LoginDTO data) {
        return ResponseEntity.status(200).body(pessoaService.autenticar(data.email(), data.senha()));
    }
*/
}