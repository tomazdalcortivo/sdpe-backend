package br.com.ifpr.edu.sdpe_backend.controller;

import br.com.ifpr.edu.sdpe_backend.domain.DTO.EstatisticaDTO;
import br.com.ifpr.edu.sdpe_backend.service.EstatisticasService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/estatisticas")
@RequiredArgsConstructor
public class EstatisticasController {

    private final EstatisticasService service;

    @GetMapping("/geral/cadastros")
    public ResponseEntity<List<EstatisticaDTO>> getCadastros() {
        return ResponseEntity.ok(service.getCadastrosMensais());
    }

    @GetMapping("/geral/total-projetos")
    public ResponseEntity<Long> getTotalProjetos() {
        return ResponseEntity.ok(service.getTotalProjetos());
    }

    @GetMapping("/geral/areas")
    public ResponseEntity<List<EstatisticaDTO>> getAreas() {
        return ResponseEntity.ok(service.getProjetosPorArea());
    }

    @GetMapping("/visualizacoes")
    public ResponseEntity<List<EstatisticaDTO>> getVisualizacoes(@RequestParam Long projetoId) {
        return ResponseEntity.ok(service.getVisualizacoesPorProjeto(projetoId));
    }
}
