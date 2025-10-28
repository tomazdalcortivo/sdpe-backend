package br.com.ifpr.edu.sdpe_backend.controller;

import br.com.ifpr.edu.sdpe_backend.domain.InstituicaoEnsino;
import br.com.ifpr.edu.sdpe_backend.service.InstituicaoEnsinoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/instituicao-ensino")
public class InstituicaoEnsinoController {

    private final InstituicaoEnsinoService instituicaoEnsinoService;

    @PostMapping
    public ResponseEntity<InstituicaoEnsino> salvar(@RequestBody InstituicaoEnsino instituicaoEnsino) {
        return ResponseEntity.ok(instituicaoEnsinoService.salvar(instituicaoEnsino));
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<InstituicaoEnsino> buscarPorNome(@PathVariable String nome) {
        return ResponseEntity.ok(instituicaoEnsinoService.buscarPorNome(nome));
    }

    @GetMapping("/cidade/{cidade}")
    public ResponseEntity<InstituicaoEnsino> buscarPorCidade(@PathVariable String cidade) {
        return ResponseEntity.ok(instituicaoEnsinoService.buscarPorCidade(cidade));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InstituicaoEnsino> buscarPorID(@PathVariable Long id) {
        return ResponseEntity.ok(instituicaoEnsinoService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InstituicaoEnsino> atualizar(@PathVariable Long id, @RequestBody InstituicaoEnsino instituicaoEnsino) {
        return ResponseEntity.ok(instituicaoEnsinoService.atualizar(instituicaoEnsino, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        this.instituicaoEnsinoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
