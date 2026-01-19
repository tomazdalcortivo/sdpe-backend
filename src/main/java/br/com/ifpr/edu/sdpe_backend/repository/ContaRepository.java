package br.com.ifpr.edu.sdpe_backend.repository;

import br.com.ifpr.edu.sdpe_backend.domain.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface ContaRepository extends JpaRepository<Conta, Long> {

    UserDetails findByEmail(String email);

    @Query("SELECT YEAR(c.dataCriacao), MONTH(c.dataCriacao), COUNT(c) " +
            "FROM Conta c " +
            "GROUP BY YEAR(c.dataCriacao), MONTH(c.dataCriacao) " +
            "ORDER BY YEAR(c.dataCriacao) ASC, MONTH(c.dataCriacao) ASC")
    List<Object[]> countCadastrosPorMes();

}
