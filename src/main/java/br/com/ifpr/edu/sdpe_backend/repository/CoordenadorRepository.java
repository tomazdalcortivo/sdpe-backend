package br.com.ifpr.edu.sdpe_backend.repository;

import br.com.ifpr.edu.sdpe_backend.domain.Coordenador;
import br.com.ifpr.edu.sdpe_backend.domain.Projeto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CoordenadorRepository extends JpaRepository<Coordenador, Long> {

    List<Coordenador> findByNomeContainingIgnoreCase(String nome);

    Optional<Coordenador> findByContaEmail(String email);

    // experimental
    Page<Projeto> findByMeusProjetosCriados(Coordenador coordenador, Pageable pageable);

}