package br.com.ifpr.edu.sdpe_backend.service;

import br.com.ifpr.edu.sdpe_backend.domain.DTO.EstatisticaDTO;
import br.com.ifpr.edu.sdpe_backend.repository.ContaRepository;
import br.com.ifpr.edu.sdpe_backend.repository.ProjetoRepository;
import br.com.ifpr.edu.sdpe_backend.repository.VisualizacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EstatisticasService {

    private final ContaRepository contaRepository;
    private final ProjetoRepository projetoRepository;
    private final VisualizacaoRepository visualizacaoRepository;

    // --- VISÃO GERAL (TODOS) ---

    public List<EstatisticaDTO> getCadastrosMensais() {
        List<Object[]> resultados = contaRepository.countCadastrosPorMes();
        return converterParaDTO(resultados);
    }

    public long getTotalProjetos() {
        return projetoRepository.count();
    }

    public List<EstatisticaDTO> getProjetosPorArea() {
        List<Object[]> resultados = projetoRepository.countProjetosPorArea();
        // Area já vem como String na query, não precisa converter mês
        return resultados.stream()
                .map(obj -> new EstatisticaDTO((String) obj[0], (Long) obj[1]))
                .collect(Collectors.toList());
    }

    public List<EstatisticaDTO> getVisualizacoesPorProjeto(Long projetoId) {
        List<Object[]> resultados = visualizacaoRepository.countVisualizacoesPorMes(projetoId);
        return converterParaDTO(resultados);
    }

    private List<EstatisticaDTO> converterParaDTO(List<Object[]> dadosBrutos) {
        return dadosBrutos.stream().map(obj -> {
            Integer ano = (Integer) obj[0];
            Integer mes = (Integer) obj[1];
            Long qtd = (Long) obj[2];
            String label = getNomeMes(mes) + "/" + ano;
            return new EstatisticaDTO(label, qtd);
        }).collect(Collectors.toList());
    }

    private String getNomeMes(Integer mes) {
        if (mes == null) return "?";
        String[] meses = {"Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez"};
        return (mes >= 1 && mes <= 12) ? meses[mes - 1] : String.valueOf(mes);
    }
}