package br.com.ifpr.edu.sdpe_backend.repository;

import br.com.ifpr.edu.sdpe_backend.domain.Participante;
import br.com.ifpr.edu.sdpe_backend.domain.Projeto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParticipanteRepository extends JpaRepository<Participante, Long> {

    Optional<Participante> findByCpf(String cpf);

    Optional<Participante> findByNome(String nome);

    Page<Participante> findByProjetos(Projeto projeto, Pageable pageable);
}
