package br.com.ifpr.edu.sdpe_backend.repository;

import br.com.ifpr.edu.sdpe_backend.domain.Projeto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface ProjetoRepository extends JpaRepository<Projeto, Long> {

    List<Projeto> findByDataInicioGreaterThanEqualAndDataFimLessThanEqual(Date dataInicio, Date dataFim);

    List<Projeto> findByCoordenadores_Id(Long id);

    List<Projeto> findByParticipantes_Id(Long id);

    @Query("SELECT MONTH(p.dataInicio), COUNT(p) " +
            "FROM Projeto p " +
            "WHERE YEAR(p.dataInicio) = YEAR(CURRENT_DATE) " +
            "GROUP BY MONTH(p.dataInicio) " +
            "ORDER BY MONTH(p.dataInicio)")
    List<Object[]> countProjetosPorMesNoAnoAtual();

    @Query("SELECT p.area, COUNT(p) FROM Projeto p GROUP BY p.area")
    List<Object[]> countProjetosPorArea();

//    @Query("SELECT p FROM Projeto p JOIN p.coordenadores c WHERE c.conta.id = :contaId")
//    List<Projeto> findByCoordenadorContaId(Long contaId);
}