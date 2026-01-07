package br.com.ifpr.edu.sdpe_backend.controller;

import br.com.ifpr.edu.sdpe_backend.domain.Conta;
import br.com.ifpr.edu.sdpe_backend.repository.ContaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ContaRepository contaRepository;

    @GetMapping("/contas")
    public ResponseEntity<List<Conta>> listarTodasContas() {
        return ResponseEntity.ok(contaRepository.findAll());
    }

    @PatchMapping("/contas/{id}/status")
    public ResponseEntity<Void> alterarStatusConta(@PathVariable Long id, @RequestParam Boolean ativo) {
        return contaRepository.findById(id)
                .map(conta -> {
                    conta.setAtivo(ativo);
                    contaRepository.save(conta);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/contas/{id}")
    public ResponseEntity<Void> excluirConta(@PathVariable Long id) {
        if (!contaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        contaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}