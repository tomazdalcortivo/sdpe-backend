package br.com.ifpr.edu.sdpe_backend.controller;

import br.com.ifpr.edu.sdpe_backend.domain.Contato;
import br.com.ifpr.edu.sdpe_backend.domain.Projeto;
import br.com.ifpr.edu.sdpe_backend.service.ContatoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/contatos")
public class ContatoController {

    private final ContatoService contatoService;

    @PostMapping
    public ResponseEntity<Contato> salvar(@RequestBody Contato contato) {
        return ResponseEntity.ok(contatoService.salvar(contato));
    }

    @GetMapping
    public ResponseEntity<Page<Contato>> buscarTodos(
            @RequestParam(defaultValue = "0") int numPag,
            @RequestParam(defaultValue = "5") int tamPag) {

        Page<Contato> contatos = contatoService.buscarTodos(numPag, tamPag);
        return ResponseEntity.ok(contatos);
    }

    @GetMapping("/projeto")
    public ResponseEntity<Page<Contato>> buscarProjeto(
            @RequestBody Projeto projeto,
            @RequestParam(defaultValue = "0") int numPag,
            @RequestParam(defaultValue = "5") int tamPag) {

        Page<Contato> contatos = contatoService.buscarPorProjeto(projeto, numPag, tamPag);
        return ResponseEntity.ok(contatos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        this.contatoService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Contato> atualizar(@PathVariable Long id, @RequestBody Contato contato) {
        return ResponseEntity.ok(contatoService.atualizar(contato, id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contato> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(contatoService.buscarPorId(id));
    }
}