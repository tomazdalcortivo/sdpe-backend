package br.com.ifpr.edu.sdpe_backend.domain.DTO;

import br.com.ifpr.edu.sdpe_backend.domain.enums.TipoPerfil;

import java.util.Date;

public record RegisterDTO(
        String email,
        String senha,
        TipoPerfil perfil,
        String nome,
        Date dataNascimento,
        String cpf,
        String cidade,
        Boolean vinculoInstitucional) {
}