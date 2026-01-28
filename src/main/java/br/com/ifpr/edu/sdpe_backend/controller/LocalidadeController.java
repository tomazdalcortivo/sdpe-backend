package br.com.ifpr.edu.sdpe_backend.controller;

import br.com.ifpr.edu.sdpe_backend.domain.DTO.CidadeDTO;
import br.com.ifpr.edu.sdpe_backend.domain.DTO.EstadoDTO;
import br.com.ifpr.edu.sdpe_backend.service.LocalidadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/localidades")
@RequiredArgsConstructor

public class LocalidadeController {
    private final LocalidadeService localidadeService;

    @GetMapping("/estados")
    public List<EstadoDTO> listarEstados() {
        return localidadeService.buscarEstados();
    }

    @GetMapping("/estados/{uf}/cidades")
    public List<CidadeDTO> listarCidades(@PathVariable String uf) {
        return localidadeService.buscarCidadesPorUf(uf);
    }
}
