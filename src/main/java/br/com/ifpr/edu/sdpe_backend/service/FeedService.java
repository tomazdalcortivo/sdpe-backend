package br.com.ifpr.edu.sdpe_backend.service;

import br.com.ifpr.edu.sdpe_backend.domain.Coordenador;
import br.com.ifpr.edu.sdpe_backend.domain.Participante;
import br.com.ifpr.edu.sdpe_backend.domain.DTO.FeedPostDTO;
import br.com.ifpr.edu.sdpe_backend.repository.ParticipanteRepository;
import br.com.ifpr.edu.sdpe_backend.repository.ProjetoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final ProjetoRepository projetoRepository;

    public Page<FeedPostDTO> buscarFeed(Pageable pageable) {

        return projetoRepository.findAll(pageable)
                .map(projeto -> {

                    String autores;
                    if (projeto.getCoordenadores() == null || projeto.getCoordenadores().isEmpty())
                        autores = "Coordenador não informado";
                    else autores = projeto.getCoordenadores().stream()
                            .map(Coordenador::getNome)
                            .collect(Collectors.joining(","));

                    return new FeedPostDTO(
                            projeto.getId(),
                            projeto.getNome(),
                            projeto.getDescricao(),
                            projeto.getImagemPath(),
                            autores,
                            projeto.getDataInicio()
                    );
                });
    }

}