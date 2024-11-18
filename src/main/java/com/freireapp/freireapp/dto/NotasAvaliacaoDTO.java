package com.freireapp.freireapp.dto;

public record NotasAvaliacaoDTO(
    int idAluno,
    int idProfessor,
    float nota,
    String feedback
) {
}
