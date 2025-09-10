package br.com.ifpr.edu.sdpe_backend.repository;

import br.com.ifpr.edu.sdpe_backend.domain.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface ContaRepository extends JpaRepository<Conta, Long > {

    UserDetails findByLogin(String login);

    String login(String login);
}
