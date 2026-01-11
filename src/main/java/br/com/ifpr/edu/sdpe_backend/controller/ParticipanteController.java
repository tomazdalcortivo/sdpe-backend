package br.com.ifpr.edu.sdpe_backend.controller;

import br.com.ifpr.edu.sdpe_backend.domain.DTO.ParticipanteUpdateDTO;
import br.com.ifpr.edu.sdpe_backend.domain.Participante;
import br.com.ifpr.edu.sdpe_backend.domain.Projeto;
import br.com.ifpr.edu.sdpe_backend.service.ParticipanteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/participantes")
public class ParticipanteController {

    private final ParticipanteService participanteService;

    @PostMapping
    public ResponseEntity<Participante> salvar(@RequestBody @Valid Participante participante) {
        this.participanteService.salvar(participante);
        return ResponseEntity.ok(participante);
    }

    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<Participante> buscarPorCpf(@PathVariable String cpf) {
        Participante participante = this.participanteService.buscarPorCpf(cpf);
        return ResponseEntity.ok(participante);
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<Participante> buscarPorNome(@PathVariable String nome) {
        Participante participante = this.participanteService.buscarPorNome(nome);
        return ResponseEntity.ok(participante);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Participante> atualizar(@RequestBody @Valid ParticipanteUpdateDTO participante, @PathVariable Long id) {
        this.participanteService.atualizar(participante, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<Participante>> buscarParticipantePorProjeto(
            @RequestBody Projeto projeto,
            @RequestParam(defaultValue = "0") int numPag,
            @RequestParam(defaultValue = "5") int tamPag) {
        Page<Participante> participantes = this.participanteService.listarPorProjeto(projeto, numPag, tamPag);
        return ResponseEntity.ok(participantes);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        this.participanteService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/foto")
    public ResponseEntity<Participante> atualizarFoto(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {
        try {
            Participante atualizado = this.participanteService.uploadFotoPerfil(id, file);
            return ResponseEntity.ok(atualizado);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}