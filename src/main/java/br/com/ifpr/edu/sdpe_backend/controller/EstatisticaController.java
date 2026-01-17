package br.com.ifpr.edu.sdpe_backend.controller;

import br.com.ifpr.edu.sdpe_backend.domain.DTO.EstatisticaDTO;
import br.com.ifpr.edu.sdpe_backend.service.ProjetoService;
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
public class EstatisticaController {

    private final ProjetoService projetoService;

    @GetMapping("/visualizacoes")
    public ResponseEntity<List<EstatisticaDTO>> getVisualizacoes(
            @RequestParam(required = false) Long projetoId) {
        return ResponseEntity.ok(projetoService.getEstatisticasVisualizacoes(projetoId));
    }

    @GetMapping("/areas")
    public ResponseEntity<List<EstatisticaDTO>> getAreas() {
        return ResponseEntity.ok(projetoService.getEstatisticasPorArea());
    }
}
