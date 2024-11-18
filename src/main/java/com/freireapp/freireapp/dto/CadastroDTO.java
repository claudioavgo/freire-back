package com.freireapp.freireapp.dto;

public record CadastroDTO(
        String nome,
        String rua,
        int numero,
        String cidade,
        String telefone1,
        String telefone2,
        String email,
        String senha,
        String dataNascimento,
        int tipo,
        int periodo,
        String especializacao,
        int idSecretaria
) {
}
