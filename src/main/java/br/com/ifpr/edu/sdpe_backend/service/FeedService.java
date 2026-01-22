package br.com.ifpr.edu.sdpe_backend.service;

import br.com.ifpr.edu.sdpe_backend.domain.Coordenador;
import br.com.ifpr.edu.sdpe_backend.domain.Seguidor;
import br.com.ifpr.edu.sdpe_backend.domain.Participante;
import br.com.ifpr.edu.sdpe_backend.domain.Projeto;
import br.com.ifpr.edu.sdpe_backend.domain.DTO.FeedPostDTO;
import br.com.ifpr.edu.sdpe_backend.repository.SeguidorRepository;
import br.com.ifpr.edu.sdpe_backend.repository.ParticipanteRepository;
import br.com.ifpr.edu.sdpe_backend.repository.ProjetoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final ProjetoRepository projetoRepository;
    private final SeguidorRepository seguidorRepository;
    private final ParticipanteRepository participanteRepository;

    public Page<FeedPostDTO> buscarFeed(Pageable pageable) {
        String emailUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
        Participante logado = participanteRepository.findByContaEmail(emailUsuario).orElse(null);

        return projetoRepository.findAll(pageable)
                .map(projeto -> {
                    boolean seguindo = false;
                    if (logado != null) {
                        seguindo = seguidorRepository.existsByProjetoIdAndParticipanteId(projeto.getId(), logado.getId());
                    }
                    Long totalSeguidores = seguidorRepository.countByProjetoId(projeto.getId());

                    String autores;
                    if (projeto.getCoordenadores() == null || projeto.getCoordenadores().isEmpty()) autores = "Coordenador não informado";
                    else autores = projeto.getCoordenadores().stream()
                            .map(Coordenador::getNome)
                            .collect(Collectors.joining(","));

                    return new FeedPostDTO(
                            projeto.getId(),
                            projeto.getNome(),
                            projeto.getDescricao(),
                            projeto.getImagemPath(),
                            autores,
                            projeto.getDataInicio(),
                            totalSeguidores,
                            seguindo
                    );
                });
    }

    public void alternarSeguir(Long projetoId) {
        String emailUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
        Participante logado = participanteRepository.findByContaEmail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Projeto projeto = projetoRepository.findById(projetoId)
                .orElseThrow(() -> new RuntimeException("Projeto não encontrado"));

        var seguidorExistente = seguidorRepository.findByProjetoIdAndParticipanteId(projetoId, logado.getId());

        if (seguidorExistente.isPresent()) {
            seguidorRepository.delete(seguidorExistente.get());
        } else {
            Seguidor novo = new Seguidor();
            novo.setProjeto(projeto);
            novo.setParticipante(logado);
            seguidorRepository.save(novo);
        }
    }
}