package com.freireapp.freireapp.dto;

public record CadastroDTO(
    String nome,
    String rua,
    String numero,
    String cidade,
    String telefone1,
    String telefone2,
    String email,
    String senha,
    String dataNascimento,
    String tipo
) {
}
