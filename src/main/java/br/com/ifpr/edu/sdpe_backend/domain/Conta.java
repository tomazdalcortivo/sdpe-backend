package br.com.ifpr.edu.sdpe_backend.domain;

import br.com.ifpr.edu.sdpe_backend.domain.enums.AccountRole;
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

    private AccountRole role;

    public Conta(String login, String senha, AccountRole role) {
        this.login = login;
        this.role = role;
        this.senha = senha;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.role == AccountRole.CORDENADOR) return List.of(new SimpleGrantedAuthority("ROLE_CORDENADOR"));
        else return List.of(new SimpleGrantedAuthority("ALUNO"));
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
