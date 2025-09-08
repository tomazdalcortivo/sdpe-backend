package br.com.ifpr.edu.sdpe_backend.domain.enums;

public enum AccountRole {

    CORDENADOR("ROLE_CORDENADOR"),
    ALUNO("ROLE_ALUNO");

    private String role;

    AccountRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
