package br.com.ifpr.edu.sdpe_backend.controller;

import br.com.ifpr.edu.sdpe_backend.domain.Coordenador;
import br.com.ifpr.edu.sdpe_backend.domain.Participante;
import br.com.ifpr.edu.sdpe_backend.domain.Projeto;
import br.com.ifpr.edu.sdpe_backend.service.CoordenadorService;
import br.com.ifpr.edu.sdpe_backend.service.ParticipanteService;
import br.com.ifpr.edu.sdpe_backend.service.ProjetoService;
//import br.com.ifpr.edu.sdpe_backend.util.UploadUtil;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
            @RequestPart(value = "arquivo", required = false) MultipartFile arquivo
    ) throws IOException {

        Projeto salvo = projetoService.salvar(projeto, arquivo);
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
    public ResponseEntity<Projeto> buscarPorId(@PathVariable Long id) {
        Projeto projeto = this.projetoService.buscarPorId(id);
        return ResponseEntity.ok(projeto);
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

    @PutMapping("/{id}")
    public ResponseEntity<Projeto> atualizar(@RequestBody Projeto projeto, @PathVariable Long id) {
        this.projetoService.atualizar(projeto, id);
        return ResponseEntity.noContent().build();
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
}