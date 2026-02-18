package br.com.ifpr.edu.sdpe_backend.controller;

import br.com.ifpr.edu.sdpe_backend.domain.*;
import br.com.ifpr.edu.sdpe_backend.domain.DTO.FeedbackResponseDTO;
import br.com.ifpr.edu.sdpe_backend.domain.enums.TipoContato;
import br.com.ifpr.edu.sdpe_backend.service.CoordenadorService;
import br.com.ifpr.edu.sdpe_backend.service.ParticipanteService;
import br.com.ifpr.edu.sdpe_backend.service.PostService;
import br.com.ifpr.edu.sdpe_backend.service.ProjetoService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projetos")
public class ProjetoController {

    private final ProjetoService projetoService;

    private final CoordenadorService coordenadorService;

    private final ParticipanteService participanteService;

    private final PostService postService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Projeto> salvar(
            @RequestPart("projeto") Projeto projeto,
            @RequestPart(value = "arquivo") MultipartFile arquivo,
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
    public ResponseEntity<Resource> getImagem(@PathVariable Long id) throws MalformedURLException {
        Projeto projeto = projetoService.buscarPorId(id);
        File arquivo = new File(projeto.getImagemPath());
        if (!arquivo.exists()) return ResponseEntity.notFound().build();
        UrlResource resource = new UrlResource(arquivo.toURI());
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);
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
            @RequestPart("projeto") String projetoJson,
            @RequestPart(value = "imagem", required = false) MultipartFile imagem,
            @RequestPart(value = "documentos", required = false) List<MultipartFile> documentos) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            Projeto projeto = mapper.readValue(projetoJson, Projeto.class);

            return ResponseEntity.ok(projetoService.atualizar(projeto, id, imagem, documentos));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
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

    @PostMapping("/{id}/posts")
    public ResponseEntity<Post> criarPost(
            @PathVariable Long id,
            @RequestPart("conteudo") String conteudo,
            @RequestPart(value = "arquivo", required = false) MultipartFile arquivo,
            Principal principal
    ) throws IOException {
        Coordenador autor = coordenadorService.buscarPorEmail(principal.getName());
        Post post = postService.criarPost(id, conteudo, arquivo, autor.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(post);
    }

    @PutMapping("/{id}/posts/{postId}")
    public ResponseEntity<Post> editarPost(
            @PathVariable Long id,
            @PathVariable Long postId,
            @RequestBody Map<String, String> payload,
            Principal principal
    ) {
        Coordenador coordenador = coordenadorService.buscarPorEmail(principal.getName());

        String novoConteudo = payload.get("conteudo");
        Post postAtualizado = postService.atualizarPost(id, postId, novoConteudo, coordenador.getId());

        return ResponseEntity.ok(postAtualizado);
    }

    @DeleteMapping("/{id}/posts/{postId}")
    public ResponseEntity<Void> excluirPost(
            @PathVariable Long id,
            @PathVariable Long postId,
            Principal principal
    ) {
        Coordenador coordenador = coordenadorService.buscarPorEmail(principal.getName());

        postService.excluirPost(id, postId, coordenador.getId());

        return ResponseEntity.noContent().build();
    }


    @GetMapping("/{id}/feedbacks")
    public ResponseEntity<List<FeedbackResponseDTO>> listarFeedbacks(@PathVariable Long id) {
        List<FeedbackResponseDTO> feedbacks = projetoService.listarFeedbacks(id);
        return ResponseEntity.ok(feedbacks);
    }

    @PostMapping("/{id}/feedbacks")
    public ResponseEntity<Contato> adicionarFeedback(@PathVariable Long id, @RequestBody Contato contato) {
        contato.setTipoContato(TipoContato.FEEDBACK);
        Contato novoFeedback = projetoService.adicionarFeedback(id, contato);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoFeedback);
    }

    @PutMapping("/{id}/feedbacks/{idFeedback}")
    public ResponseEntity<Contato> editarFeedback(
            @PathVariable Long id,
            @PathVariable Long idFeedback,
            @RequestBody Map<String, String> payload,
            Principal principal
    ) {
        String novaMensagem = payload.get("mensagem");
        String emailLogado = principal.getName();
        Contato atualizado = projetoService.editarFeedback(id, idFeedback, novaMensagem, emailLogado);

        return ResponseEntity.ok(atualizado);
    }


    @DeleteMapping("/{id}/feedbacks/{idFeedback}")
    public ResponseEntity<Void> removerFeedback(
            @PathVariable Long id,
            @PathVariable Long idFeedback,
            Principal principal
    ) {
        String emailLogado = principal.getName();
        projetoService.removerFeedback(id, idFeedback, emailLogado);
        return ResponseEntity.noContent().build();
    }
}