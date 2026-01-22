package br.com.ifpr.edu.sdpe_backend.repository;
import br.com.ifpr.edu.sdpe_backend.domain.Contato;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContatoRepository extends JpaRepository<Contato, Long> {

    void deleteByProjetoId(Long id);

    List<Contato> findByProjetoId(Long id);

}