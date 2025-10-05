package br.com.ifpr.edu.sdpe_backend.controller;

import br.com.ifpr.edu.sdpe_backend.domain.Projeto;
import br.com.ifpr.edu.sdpe_backend.repository.ProjetoRepository;
import br.com.ifpr.edu.sdpe_backend.service.ProjetoService;
import br.com.ifpr.edu.sdpe_backend.util.UploadUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projetos")
public class ProjetoController {

    private final ProjetoService projetoService;

    @PostMapping
    public ResponseEntity<Projeto> salvar(@RequestBody Projeto projeto) {
        Projeto projetoCriado = this.projetoService.salvar(projeto);
        return new ResponseEntity<>(projetoCriado, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<Projeto> buscarPorId(@PathVariable Long id) {
        Projeto projeto = this.projetoService.buscarPorId(id);
        return ResponseEntity.ok(projeto);
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
    ProjetoRepository projetoRepository;

    @PostMapping("/cadastro-projeto")
    public ModelAndView cadastrarProjeto(@ModelAttribute Projeto projeto, @RequestParam("file") MultipartFile imagem) {
        ModelAndView mv = new ModelAndView("projeto/cadastro");
        mv.addObject("projeto", projeto);

        try {
            if(UploadUtil.fazerUploadImagem(imagem)){
                projeto.setImagem(imagem.getOriginalFilename());
            }
            projetoRepository.save(projeto);
            System.out.println("Salvo com sucesso");
            return new ModelAndView("redirect:/home");

        } catch (Exception e) {
            mv.addObject("Erro", e.getMessage());
            System.out.println("Erro ao salvar" + e.getMessage());
            return mv;
        }
    }
}
