package br.com.ifpr.edu.sdpe_backend.repository;

import br.com.ifpr.edu.sdpe_backend.domain.Coordenador;
import br.com.ifpr.edu.sdpe_backend.domain.Projeto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CoordenadorRepository extends JpaRepository<Coordenador, Long> {

    Optional<Coordenador> findByCpf(String cpf);

    Optional<Coordenador> findByNome(String nome);

    // experimental
    Page<Projeto> findByProjetos(Coordenador coordenador, Pageable pageable);

    Optional<Coordenador> findByContato(String contato);
}