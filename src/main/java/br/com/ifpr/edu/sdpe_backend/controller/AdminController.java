package br.com.ifpr.edu.sdpe_backend.controller;

import br.com.ifpr.edu.sdpe_backend.domain.Contato;
import br.com.ifpr.edu.sdpe_backend.domain.Participante;
import br.com.ifpr.edu.sdpe_backend.domain.Projeto;
import br.com.ifpr.edu.sdpe_backend.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/contas")
    public ResponseEntity<List<Participante>> listarTodosUsuarios() {
        return ResponseEntity.ok(adminService.listarTodosUsuarios());
    }

    @GetMapping("/solicitacoes-pendentes")
    public ResponseEntity<List<Participante>> listarPendentes() {
        return ResponseEntity.ok(adminService.listarPendentes());
    }

    @PatchMapping("/contas/{id}/status")
    public ResponseEntity<Void> alterarStatusConta(@PathVariable Long id, @RequestParam Boolean ativo) {
        try {
            adminService.alterarStatusConta(id, ativo);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/contas/{id}")
    public ResponseEntity<Void> excluirConta(@PathVariable Long id) {
        try {
            adminService.excluirConta(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/projetos")
    public ResponseEntity<List<Projeto>> listarTodosProjetos() {
        return ResponseEntity.ok(adminService.listarTodosProjetos());
    }

    @DeleteMapping("/projetos/{id}")
    public ResponseEntity<Void> excluirProjeto(@PathVariable Long id) {
        try {
            adminService.excluirProjeto(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/contatos")
    public ResponseEntity<List<Contato>> listarContatos() {
        return ResponseEntity.ok(adminService.listarTodosContatos());
    }
}