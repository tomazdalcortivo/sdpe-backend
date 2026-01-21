package br.com.ifpr.edu.sdpe_backend.controller;

import br.com.ifpr.edu.sdpe_backend.domain.Coordenador;
import br.com.ifpr.edu.sdpe_backend.domain.Participante;
import br.com.ifpr.edu.sdpe_backend.domain.Projeto;
import br.com.ifpr.edu.sdpe_backend.service.CoordenadorService;
import br.com.ifpr.edu.sdpe_backend.service.ParticipanteService;
import br.com.ifpr.edu.sdpe_backend.service.ProjetoService;
import org.springframework.core.io.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.security.Principal;
import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projetos")
public class ProjetoController {

    private final ProjetoService projetoService;

    private final CoordenadorService coordenadorService;

    private final ParticipanteService participanteService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Projeto> salvar(
            @RequestPart("projeto") Projeto projeto,
            @RequestPart(value = "arquivo", required = false) MultipartFile arquivo,
            Principal principal
    ) throws IOException {

        String emailCoordenador = (principal != null) ? principal.getName() : null;

        Projeto salvo = projetoService.salvar(projeto, arquivo, emailCoordenador);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @GetMapping("/meus-criados")
    public ResponseEntity<List<Projeto>> listarMeusProjetosCriados(Principal principal) {
        Coordenador coord = coordenadorService.buscarPorEmail(principal.getName());
        return ResponseEntity.ok(projetoService.buscarPorCoordenador(coord.getId()));
    }

    @GetMapping("/meus-participados")
    public ResponseEntity<List<Projeto>> listarMeusProjetosParticipados(Principal principal) {
        Participante part = participanteService.buscarPorEmail(principal.getName());
        return ResponseEntity.ok(projetoService.buscarPorParticipante(part.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Projeto> buscarPorId(@PathVariable("id") Long id) {
        Projeto projeto = this.projetoService.buscarPorId(id);
        return ResponseEntity.ok(projeto);
    }

    @PostMapping("/{id}/visualizacao")
    public ResponseEntity<Void> registrarVisualizacao(@PathVariable("id") Long id) {
        this.projetoService.registrarVisualizacao(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/imagem")
    public ResponseEntity<Resource> getImagem(@PathVariable Long id) throws MalformedURLException, MalformedURLException {
        Projeto projeto = projetoService.buscarPorId(id);
        File arquivo = new File(projeto.getImagemPath());
        if (!arquivo.exists()) return ResponseEntity.notFound().build();
        UrlResource resource = new UrlResource(arquivo.toURI());
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG) // ou IMAGE_PNG
                .body((Resource) resource);
    }

    @GetMapping
    public ResponseEntity<Page<Projeto>> buscarTodos(
            @RequestParam(defaultValue = "0") int numPag,
            @RequestParam(defaultValue = "5") int tamPag) {

        Page<Projeto> projetos = projetoService.buscarTodos(numPag, tamPag);
        return ResponseEntity.ok(projetos);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Projeto> atualizar(
            @PathVariable Long id,
            @RequestPart("projeto") Projeto projeto,
            @RequestPart(value = "imagem", required = false) MultipartFile imagem,
            Principal principal) throws IOException {

        if (principal != null) {
            Coordenador coordLogado = coordenadorService.buscarPorEmail(principal.getName());
            Projeto projetoExistente = projetoService.buscarPorId(id);

            boolean isCoordenadorDoProjeto = projetoExistente.getCoordenadores().stream()
                    .anyMatch(c -> c.getId().equals(coordLogado.getId()));

            // Se não for coordenador do projeto (e assumindo que não temos role ADMIN separada aqui no check), lança erro
            if (!isCoordenadorDoProjeto) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

        }

        Projeto atualizado = this.projetoService.atualizar(projeto, id, imagem);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        this.projetoService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/data-inicio/{dataInicio}")
    public ResponseEntity<List<Projeto>> buscarPorPeriodo(@PathVariable Date dataInicio, Date dataFim) {
        return ResponseEntity.ok(projetoService.buscarPorPeriodo(dataInicio, dataFim));
    }

    @PostMapping("/{id}/participantes/{idParticipante}")
    public ResponseEntity<Void> adicionarParticipante(@PathVariable Long id, @PathVariable Long idParticipante) {
        projetoService.adicionarParticipante(id, idParticipante);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/participantes/{idParticipante}")
    public ResponseEntity<Void> removerParticipante(@PathVariable Long id, @PathVariable Long idParticipante) {
        projetoService.removerParticipante(id, idParticipante);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/coordenadores/{idCoordenador}")
    public ResponseEntity<Void> adicionarCoordenador(@PathVariable Long id, @PathVariable Long idCoordenador) {
        projetoService.adicionarCoordenador(id, idCoordenador);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/coordenadores/{idCoordenador}")
    public ResponseEntity<Void> removerCoordenador(@PathVariable Long id, @PathVariable Long idCoordenador) {
        projetoService.removerCoordenador(id, idCoordenador);
        return ResponseEntity.noContent().build();
    }
}