package br.com.ifpr.edu.sdpe_backend.domain;

import br.com.ifpr.edu.sdpe_backend.domain.enums.TipoPerfil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tb_conta")
public class Conta implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Email(message = "Digite um email valido")
    @NotBlank(message = "email não pode ser vazio")
    private String email;

    private String codigoRecuperacao;

    private Instant dataExpiracaoCodigo;

    @NotBlank(message = "Senha não pode ser vazia")
    private String senha;

    @Enumerated(EnumType.STRING)
    private TipoPerfil perfil;

    @OneToOne(mappedBy = "conta")
    @JsonIgnore
    private Participante participante;

    @CreationTimestamp
    private Instant dataCriacao;

    private Boolean ativo;

    public Conta(){
        email  = " ";
        senha = " ";
        ativo = true;
        dataCriacao =  Instant.now();
        perfil = TipoPerfil.PARTICIPANTE;
    }

    public Conta(String email, String senha, TipoPerfil perfil) {
        this.email = email;
        this.perfil = perfil;
        this.senha = senha;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.perfil == TipoPerfil.ADMIN) {
            return List.of(
                    new SimpleGrantedAuthority("ROLE_ADMIN"),
                    new SimpleGrantedAuthority("ROLE_COORDENADOR"),
                    new SimpleGrantedAuthority("ROLE_PARTICIPANTE")
            );
        } else if (this.perfil == TipoPerfil.COORDENADOR) return List.of(new SimpleGrantedAuthority("ROLE_COORDENADOR"));
        else return List.of(new SimpleGrantedAuthority("ROLE_PARTICIPANTE"));
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.email;
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