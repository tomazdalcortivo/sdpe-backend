package br.com.ifpr.edu.sdpe_backend.domain.DTO;

import br.com.ifpr.edu.sdpe_backend.domain.enums.TipoContato;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Instant;

public record FeedbackResponseDTO(
        Long id,
        String nome,
        String email,
        String mensagem,
        TipoContato tipoContato,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "America/Sao_Paulo")
        Instant dataEnvio,
        String fotoPerfil
) {}