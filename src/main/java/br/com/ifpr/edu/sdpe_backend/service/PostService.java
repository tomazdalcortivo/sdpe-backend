package br.com.ifpr.edu.sdpe_backend.service;

import br.com.ifpr.edu.sdpe_backend.domain.Coordenador;
import br.com.ifpr.edu.sdpe_backend.domain.Participante;
import br.com.ifpr.edu.sdpe_backend.domain.Post;
import br.com.ifpr.edu.sdpe_backend.domain.Projeto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {

    private final ProjetoService projetoService;

    private final CoordenadorService coordenadorService;

    private final ParticipanteService participanteService;

    private final Path rootLocation = Paths.get("uploads");

    public Post criarPost(Long idProjeto, String conteudo, MultipartFile arquivo, Long idCoordenador) throws IOException {
        Projeto projeto = this.projetoService.buscarPorId(idProjeto);
        Coordenador autor = this.coordenadorService.buscarPorId(idCoordenador);

        if (!projeto.getCoordenadores().contains(autor))
            throw new IllegalArgumentException("Apenas coordenadores do projeto podem criar posts.");

        Post post = Post.builder()
                .conteudo(conteudo)
                .dataPublicacao(new Date())
                .autor(autor)
                .projeto(projeto)
                .build();

        if (arquivo != null && !arquivo.isEmpty()) {
            if (!Files.exists(rootLocation)) Files.createDirectory(rootLocation);

            String filename = "post-" + UUID.randomUUID() + "-" + arquivo.getOriginalFilename();
            Path destinationPath = rootLocation.resolve(filename);

            Files.copy(arquivo.getInputStream(), destinationPath, StandardCopyOption.REPLACE_EXISTING);

            String fileUrl = "http://localhost:8080/imagens/" + filename;
            post.setMediaUrl(fileUrl);
        }

        projeto.getPosts().add(post);
        this.projetoService.salvar(projeto);
        return post;
    }

    public void alternarSeguir(Long idProjeto,Long idParticipante){
        Projeto projeto = this.projetoService.buscarPorId(idProjeto);
        Participante participante = this.participanteService.buscarPorId(idParticipante);

        if (projeto.getSeguidores().contains(participante)){
            projeto.getSeguidores().remove(participante);
        }else {
            projeto.getSeguidores().add(participante);
        }
        this.projetoService.salvar(projeto);
    }
}
