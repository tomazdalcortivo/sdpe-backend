package br.com.ifpr.edu.sdpe_backend.domain.DTO;

import br.com.ifpr.edu.sdpe_backend.domain.enums.TipoContato;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ContatoPublicoDTO(
        @NotBlank String nome,
        @NotBlank @Email String email,
        @NotBlank String mensagem,
        @NotNull TipoContato tipoContato,
        Long projetoId,
        String altcha
) {}
