package br.com.ifpr.edu.sdpe_backend.domain.DTO;

import br.com.ifpr.edu.sdpe_backend.domain.enums.TipoPerfil;

public record UsuarioResponseDTO(
        Long id,
        String nome,
        String email,
        String telefone,
        String cidade,
        String resumo,
        String fotoPerfil,
        TipoPerfil perfil
) {}