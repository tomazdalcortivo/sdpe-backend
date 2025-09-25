package br.com.ifpr.edu.sdpe_backend.domain;

import br.com.ifpr.edu.sdpe_backend.domain.enums.PerfilConta;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_conta")
public class Conta implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    private String login;

    private String senha;

    private PerfilConta perfil;

    @OneToOne(mappedBy = "conta", cascade = CascadeType.ALL)
    private Coordenador coordenador;

    @OneToOne(mappedBy = "conta", cascade = CascadeType.ALL)
    private Participante participante;

    public Conta(String login, String senha, PerfilConta perfil) {
        this.login = login;
        this.perfil = perfil;
        this.senha = senha;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.perfil == PerfilConta.ADMIN) {
            return List.of(
                    new SimpleGrantedAuthority("PERFIL_ADMIN"),
                    new SimpleGrantedAuthority("PERFIL_COORDENADOR"),
                    new SimpleGrantedAuthority("PERFIL_PARTICIPANTE")
            );
        } else if (this.perfil == PerfilConta.COORDENADOR) return List.of(new SimpleGrantedAuthority("PERFIL_COORDENADOR"));
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
