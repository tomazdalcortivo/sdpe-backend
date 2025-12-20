package br.com.ifpr.edu.sdpe_backend.repository;

import br.com.ifpr.edu.sdpe_backend.domain.Projeto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface ProjetoRepository extends JpaRepository<Projeto, Long> {

    List<Projeto> findByDataInicioGreaterThanEqualAndDataFimLessThanEqual(Date dataInicio, Date dataFim);
}