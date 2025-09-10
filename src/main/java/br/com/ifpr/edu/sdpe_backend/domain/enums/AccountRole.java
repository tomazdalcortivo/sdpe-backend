package br.com.ifpr.edu.sdpe_backend.domain.enums;

public enum AccountRole {

    ADMIN("ROLE_ADMIN"),
    COORDENADOR("ROLE_COORDENADOR"),
    PARTICIPANTE("ROLE_PARTICIPANTE");

    private String role;

    AccountRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
