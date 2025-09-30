package br.com.ifpr.edu.sdpe_backend.domain;

import br.com.ifpr.edu.sdpe_backend.domain.enums.TipoPerfil;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Builder
@Table(name = "tb_conta")
public class Conta implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    private String email;

    private String senha;

    private String login;

    private boolean ativo;

    @CreationTimestamp
    private Instant dataCriacao;

    @Enumerated(EnumType.STRING)
    private TipoPerfil perfil;

    @OneToOne(mappedBy = "conta")
    private Participante participante;

    public Conta(){
        email  = " ";
        senha = " ";
        ativo = true;
        dataCriacao =  Instant.now();
        perfil = TipoPerfil.ADMIN;
        //participante = new Participante();
    }

    public Conta(String login, String senha, TipoPerfil perfil) {
        this.login = login;
        this.perfil = perfil;
        this.senha = senha;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.perfil == TipoPerfil.ADMIN) {
            return List.of(
                    new SimpleGrantedAuthority("PERFIL_ADMIN"),
                    new SimpleGrantedAuthority("PERFIL_COORDENADOR"),
                    new SimpleGrantedAuthority("PERFIL_PARTICIPANTE")
            );
        } else if (this.perfil == TipoPerfil.COORDENADOR) return List.of(new SimpleGrantedAuthority("PERFIL_COORDENADOR"));
        else return List.of(new SimpleGrantedAuthority("PERFIL_PARTICIPANTE"));
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}