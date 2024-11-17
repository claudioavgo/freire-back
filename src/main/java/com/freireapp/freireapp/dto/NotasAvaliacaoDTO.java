package com.freireapp.freireapp.dto;

public record NotasAvaliacaoDTO(
    int idAluno,
    int idProfessor,
    int idAvaliacao,
    float nota,
    String feedback
) {
}
