package br.com.ifpr.edu.sdpe_backend.domain.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ParticipanteUpdateDTO(
        @Pattern(
                regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ]+( [A-Za-zÀ-ÖØ-öø-ÿ]+)+$",
                message = "Digite o nome completo (nome e sobrenome)"
        )
        @NotBlank(message = "O nome não pode ser vazio")
        String nome,

        String telefone,

        String cidade,

        String resumo
) {
}