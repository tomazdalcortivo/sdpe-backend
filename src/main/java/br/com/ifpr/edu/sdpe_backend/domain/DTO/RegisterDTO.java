package br.com.ifpr.edu.sdpe_backend.domain.DTO;

import br.com.ifpr.edu.sdpe_backend.domain.enums.TipoPerfil;

public record RegisterDTO(String login,
                          String senha,
                          TipoPerfil perfil,
                          String nome,
                          String contato,
                          String telefone) {
}