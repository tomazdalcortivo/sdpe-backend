package br.com.ifpr.edu.sdpe_backend.domain.DTO;

import br.com.ifpr.edu.sdpe_backend.domain.enums.AccountRole;

public record RegisterDTO(String login, String senha, AccountRole role) {
}
