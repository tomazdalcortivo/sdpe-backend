package br.com.ifpr.edu.sdpe_backend.domain.enums;

public enum TipoPerfil {

    ADMIN("ROLE_ADMIN"),
    COORDENADOR("ROLE_COORDENADOR"),
    PARTICIPANTE("ROLE_PARTICIPANTE");

    private String perfil;

    TipoPerfil(String perfil) {
        this.perfil = perfil;
    }

    public String getPerfil() {
        return perfil;
    }
}
