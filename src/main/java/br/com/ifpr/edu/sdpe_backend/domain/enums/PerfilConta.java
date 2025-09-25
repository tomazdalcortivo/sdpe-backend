package br.com.ifpr.edu.sdpe_backend.domain.enums;

public enum PerfilConta {

    ADMIN("PERFIL_ADMIN"),
    COORDENADOR("PERFIL_COORDENADOR"),
    PARTICIPANTE("PERFIL_PARTICIPANTE");

    private String perfil;

    PerfilConta(String perfil) {
        this.perfil = perfil;
    }

    public String getPerfil() {
        return perfil;
    }
}
