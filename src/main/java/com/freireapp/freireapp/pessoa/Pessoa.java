package com.freireapp.freireapp.pessoa;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Pessoa {
    private int idPessoa;
    private String nome;
    private String rua;
    private String numero;
    private String cidade;
    private String telefone1;
    private String telefone2;
    private String email;
    private String senha;
    private String dataNascimento;
    private String tipo;
}