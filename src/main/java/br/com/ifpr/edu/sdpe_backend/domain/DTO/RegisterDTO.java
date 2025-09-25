package br.com.ifpr.edu.sdpe_backend.domain.DTO;

import br.com.ifpr.edu.sdpe_backend.domain.enums.PerfilConta;

public record RegisterDTO(String login,
                          String senha,
                          PerfilConta perfil,
                          String nome,
                          String contato,
                          String telefone) {
}