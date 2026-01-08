package br.com.ifpr.edu.sdpe_backend.domain.DTO;

public record PasswordResetDTO(String email, String codigo, String novaSenha) {
}
