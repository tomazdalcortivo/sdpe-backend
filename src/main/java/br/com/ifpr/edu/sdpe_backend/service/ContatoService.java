package br.com.ifpr.edu.sdpe_backend.service;

import br.com.ifpr.edu.sdpe_backend.domain.Contato;
import br.com.ifpr.edu.sdpe_backend.domain.DTO.FeedbackResponseDTO;
import br.com.ifpr.edu.sdpe_backend.domain.Participante;
import br.com.ifpr.edu.sdpe_backend.domain.Projeto;
import br.com.ifpr.edu.sdpe_backend.repository.ContatoRepository;
import br.com.ifpr.edu.sdpe_backend.repository.ParticipanteRepository;
import br.com.ifpr.edu.sdpe_backend.repository.ProjetoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContatoService {

    private final ContatoRepository contatoRepository;

    private final ParticipanteRepository participanteRepository;

    public Contato salvar(Contato contato) {
        return contatoRepository.save(contato);
    }

    public List<FeedbackResponseDTO> buscarPorProjeto(Long projetoId) {
        List<Contato> contatos = this.contatoRepository.findByProjetoId(projetoId);

        return contatos.stream()
                .map(contato -> {
                    String foto = participanteRepository.findByContaEmail(contato.getEmail())
                            .map(Participante::getFotoPerfil)
                            .orElse(null);

                    return new FeedbackResponseDTO(
                            contato.getId(),
                            contato.getNome(),
                            contato.getEmail(),
                            contato.getMensagem(),
                            contato.getTipoContato(),
                            contato.getDataEnvio(),
                            foto
                    );
                })
                .sorted((c1, c2) -> c2.dataEnvio().compareTo(c1.dataEnvio())) // Ordena por data
                .toList();
    }
    public void excluir(Long id) {
        this.contatoRepository.deleteById(id);
    }

}
