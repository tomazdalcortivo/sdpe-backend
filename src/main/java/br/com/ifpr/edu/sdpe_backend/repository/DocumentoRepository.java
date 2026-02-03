package br.com.ifpr.edu.sdpe_backend.repository;

import br.com.ifpr.edu.sdpe_backend.domain.Documento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentoRepository extends JpaRepository<Documento, Long> {
}