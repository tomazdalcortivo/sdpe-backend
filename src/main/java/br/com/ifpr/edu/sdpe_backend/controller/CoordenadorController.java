package br.com.ifpr.edu.sdpe_backend.controller;

import br.com.ifpr.edu.sdpe_backend.domain.Coordenador;
import br.com.ifpr.edu.sdpe_backend.domain.Projeto;
import br.com.ifpr.edu.sdpe_backend.service.CoordenadorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/coordenadores")
public class CoordenadorController {

    private final CoordenadorService coordenadorService;

    @PostMapping
    public ResponseEntity<Coordenador> salvar(@RequestBody Coordenador coordenador) {
        this.coordenadorService.salvar(coordenador);
        return ResponseEntity.ok(coordenador);
    }

    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<Coordenador> buscarPorCpf(@PathVariable String cpf) {
        Coordenador coordenador = this.coordenadorService.buscarPorCpf(cpf);
        return ResponseEntity.ok(coordenador);
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<Coordenador> buscarPorNome(@PathVariable String nome) {
        Coordenador coordenador = this.coordenadorService.buscarPorNome(nome);
        return ResponseEntity.ok(coordenador);
    }

    @GetMapping("/contato/{contato}")
    public ResponseEntity<Coordenador> buscarPorContato(@PathVariable String contato) {
        Coordenador coordenador = this.coordenadorService.buscarPorContato(contato);
        return ResponseEntity.ok(coordenador);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Coordenador> atualizar(@RequestBody Coordenador coordenador, @PathVariable Long id) {
        this.coordenadorService.atualizar(coordenador, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/projeto")
    public ResponseEntity<Page<Projeto>> buscarProjetoPorCoordenador(
            @RequestBody Coordenador coordenador,
            @RequestParam(defaultValue = "0") int numPag,
            @RequestParam(defaultValue = "5") int tamPag) {

        Page<Projeto> projetos = this.coordenadorService.listarProjetos(coordenador, numPag, tamPag);
        return ResponseEntity.ok(projetos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        this.coordenadorService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
