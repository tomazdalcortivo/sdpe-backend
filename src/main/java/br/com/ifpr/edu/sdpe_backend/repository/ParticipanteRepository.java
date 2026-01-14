package br.com.ifpr.edu.sdpe_backend.repository;

import br.com.ifpr.edu.sdpe_backend.domain.Participante;
import br.com.ifpr.edu.sdpe_backend.domain.Projeto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParticipanteRepository extends JpaRepository<Participante, Long> {

    List<Participante> findByNomeContainingIgnoreCase(String nome);

    Optional<Participante> findByCpf(String cpf);

    Page<Participante> findByProjetos(Projeto projeto, Pageable pageable);
}
