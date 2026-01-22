package br.com.ifpr.edu.sdpe_backend.repository;

import br.com.ifpr.edu.sdpe_backend.domain.Seguidor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SeguidorRepository extends JpaRepository<Seguidor, Long> {

    boolean existsByProjetoIdAndParticipanteId(Long projetoId, Long participanteId);

    Optional<Seguidor> findByProjetoIdAndParticipanteId(Long projetoId, Long participanteId);

    Long countByProjetoId(Long projetoId);
}