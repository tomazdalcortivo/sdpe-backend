package br.com.ifpr.edu.sdpe_backend.controller;

import br.com.ifpr.edu.sdpe_backend.domain.Conta;
import br.com.ifpr.edu.sdpe_backend.domain.Contato;
import br.com.ifpr.edu.sdpe_backend.domain.Participante;
import br.com.ifpr.edu.sdpe_backend.domain.Projeto;
import br.com.ifpr.edu.sdpe_backend.domain.enums.TipoPerfil;
import br.com.ifpr.edu.sdpe_backend.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ContaRepository contaRepository;
    private final ParticipanteRepository participanteRepository;
    private final ProjetoRepository projetoRepository;
    private final ContatoRepository contatoRepository;
    private final VisualizacaoRepository visualizacaoRepository;

    @GetMapping("/contas")
    public ResponseEntity<List<Participante>> listarTodasContas() {
        return ResponseEntity.ok(participanteRepository.findAll());
    }

    @GetMapping("/solicitacoes-pendentes")
    public ResponseEntity<List<Participante>> listarPendentes() {
        return ResponseEntity.ok(participanteRepository.findPendentesDeAprovacao());
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

    @Transactional
    @DeleteMapping("/contas/{id}")
    public ResponseEntity<Void> excluirConta(@PathVariable Long id) {
        Conta conta = contaRepository.findById(id).orElse(null);
        if (conta == null) {
            return ResponseEntity.notFound().build();
        }

        Participante participante = conta.getParticipante();

        if (participante != null) {
            conta.setParticipante(null);
            participante.setConta(null);

            contaRepository.save(conta);

            if (conta.getPerfil() == TipoPerfil.COORDENADOR || conta.getPerfil() == TipoPerfil.ADMIN) {
                List<Projeto> projetosDoUsuario = projetoRepository.findByCoordenadoresId(participante.getId());
                if (!projetosDoUsuario.isEmpty()) {
                    projetoRepository.deleteAll(projetosDoUsuario);
                }
            }

            participanteRepository.delete(participante);
        }

        contaRepository.delete(conta);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/projetos")
    public ResponseEntity<List<Projeto>> listarTodosProjetos() {
        return ResponseEntity.ok(projetoRepository.findAll());
    }

    @Transactional
    @DeleteMapping("/projetos/{id}")
    public ResponseEntity<Void> excluirProjeto(@PathVariable Long id) {
        if (!projetoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        List<Contato> contatos = contatoRepository.findByProjetoId(id);
        if (!contatos.isEmpty()) {
            contatoRepository.deleteAll(contatos);
        }

        visualizacaoRepository.deleteByProjetoId(id);

        projetoRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}