package br.com.ifpr.edu.sdpe_backend.controller;

import br.com.ifpr.edu.sdpe_backend.domain.Projeto;
import br.com.ifpr.edu.sdpe_backend.repository.ProjetoRepository;
import br.com.ifpr.edu.sdpe_backend.service.ProjetoService;
//import br.com.ifpr.edu.sdpe_backend.util.UploadUtil;
import org.springframework.core.io.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projetos")
public class ProjetoController {

    private final ProjetoService projetoService;

//    @PostMapping
//    public ResponseEntity<Projeto> salvar(@RequestBody Projeto projeto) {
//        Projeto projetoCriado = this.projetoService.salvar(projeto);
//        return new ResponseEntity<>(projetoCriado, HttpStatus.CREATED);
//    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Projeto> salvar(
            @RequestPart("projeto") Projeto projeto,
            @RequestPart("imagem") MultipartFile imagem) throws IOException {

        if (imagem != null && !imagem.isEmpty()) {
            // Onde as imagens serão salvas
            String pastaImagens = "D:/imagemsdpe/";
            File diretorio = new File(pastaImagens);
            if (!diretorio.exists()) {
                diretorio.mkdirs();
            }

            // Recriar o nome da imagem
            String nomeArquivo = System.currentTimeMillis() + "_" + imagem.getOriginalFilename();

            // Salvar no disco
            File arquivoDestino = new File(diretorio, nomeArquivo);
            imagem.transferTo(arquivoDestino);

            // Salvar o caminho no banco
            projeto.setImagemPath(arquivoDestino.getAbsolutePath());
        }

        Projeto salvo = projetoService.salvar(projeto);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @GetMapping("{id}")
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
    public ResponseEntity<List<Projeto>> buscarTodos() {
        List<Projeto> projetos = this.projetoService.buscarTodos();
        return ResponseEntity.ok(projetos);
    }

    @PutMapping("{id}")
    public ResponseEntity<Projeto> atualizar(@RequestBody Projeto projeto, @PathVariable Long id) {
        this.projetoService.atualizar(projeto, id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        this.projetoService.excluir(id);
        return ResponseEntity.noContent().build();
    }

}
