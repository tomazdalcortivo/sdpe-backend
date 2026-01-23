package br.com.ifpr.edu.sdpe_backend.repository;

import br.com.ifpr.edu.sdpe_backend.domain.Visualizacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VisualizacaoRepository extends JpaRepository<Visualizacao, Long> {

    void deleteByProjetoId(Long projetoId);

    @Query("SELECT YEAR(v.dataVisualizacao), MONTH(v.dataVisualizacao), COUNT(v) " +
            "FROM Visualizacao v " +
            "WHERE v.projeto.id = :projetoId " +
            "GROUP BY YEAR(v.dataVisualizacao), MONTH(v.dataVisualizacao) " +
            "ORDER BY YEAR(v.dataVisualizacao) ASC, MONTH(v.dataVisualizacao) ASC")
    List<Object[]> countVisualizacoesPorMes(Long projetoId);
}