package br.com.ifpr.edu.sdpe_backend.repository;

import br.com.ifpr.edu.sdpe_backend.domain.Visualizacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VisualizacoesRepository extends JpaRepository<Visualizacao, Long> {

    @Query("SELECT MONTH(v.dataVisualizacao), COUNT(v) " +
            "FROM Visualizacao v " +
            "WHERE YEAR(v.dataVisualizacao) = YEAR(CURRENT_DATE) " +
            "GROUP BY MONTH(v.dataVisualizacao) " +
            "ORDER BY MONTH(v.dataVisualizacao)")
    List<Object[]> countVisualizacoesPorMesNoAnoAtual();

    @Query("SELECT MONTH(v.dataVisualizacao), COUNT(v) " +
            "FROM Visualizacao v " +
            "WHERE v.projeto.id = :projetoId AND YEAR(v.dataVisualizacao) = YEAR(CURRENT_DATE) " +
            "GROUP BY MONTH(v.dataVisualizacao)")
    List<Object[]> countVisualizacoesPorMesDoProjeto(Long projetoId);
}