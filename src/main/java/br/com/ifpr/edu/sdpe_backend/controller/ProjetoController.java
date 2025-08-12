package br.com.ifpr.edu.sdpe_backend.controller;

import br.com.ifpr.edu.sdpe_backend.domain.Projeto;
import br.com.ifpr.edu.sdpe_backend.service.ProjetoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projeto")
public class ProjetoController {

    private final ProjetoService projetoService;

    @GetMapping("{id}")
    public ResponseEntity<Projeto> getProjeto(@PathVariable Long id) {
        Projeto projeto = projetoService.getById(id);
        return ResponseEntity.ok(projeto);
    }

    @GetMapping
    public ResponseEntity<List<Projeto>> getProjetos() {
        List<Projeto> projetos = projetoService.getAll();
        return ResponseEntity.ok(projetos);
    }

    @PostMapping
    public ResponseEntity<Projeto> create(@RequestBody Projeto projeto) {
        projetoService.saveProjeto(projeto);
        return new ResponseEntity<>(projeto, HttpStatus.CREATED);
    }


    @PutMapping("{id}")
    public ResponseEntity<Projeto> update(@RequestBody Projeto projeto, @PathVariable Long id) {
        projetoService.updateProjeto(projeto, id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        projetoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
