package br.com.ifpr.edu.sdpe_backend.domain.enums;

public enum TipoPerfil {

    ADMIN("PERFIL_ADMIN"),
    COORDENADOR("PERFIL_COORDENADOR"),
    PARTICIPANTE("PERFIL_PARTICIPANTE");

    private String perfil;

    TipoPerfil(String perfil) {
        this.perfil = perfil;
    }

    public String getPerfil() {
        return perfil;
    }
}
