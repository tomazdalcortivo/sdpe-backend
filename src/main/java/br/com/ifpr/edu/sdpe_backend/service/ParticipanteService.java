package br.com.ifpr.edu.sdpe_backend.service;

import br.com.ifpr.edu.sdpe_backend.domain.Participante;
import br.com.ifpr.edu.sdpe_backend.domain.Projeto;
import br.com.ifpr.edu.sdpe_backend.exception.EntityNotFoundException;
import br.com.ifpr.edu.sdpe_backend.repository.ParticipanteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ParticipanteService {

    private final ParticipanteRepository participanteRepository;

    public Participante salvar(Participante participante) {
        return this.participanteRepository.save(participante);
    }

    public Participante buscarPorNome(String nome) {
        return this.participanteRepository.findByNome(nome).orElseThrow(
                () -> new EntityNotFoundException("Participante não encontrado"));
    }

    public Participante buscarPorCpf(String cpf) {
        return this.participanteRepository.findByCpf(cpf).orElseThrow(
                () -> new EntityNotFoundException("Participante não encontrado"));
    }

    public Page<Participante> listarPorProjeto(Projeto projeto, int numPag, int tamPag) {
        Pageable pageable = PageRequest.of(numPag, tamPag);
        return this.participanteRepository.findByProjetos(projeto, pageable);
    }

    public void excluir(Long id) {
        this.participanteRepository.deleteById(id);
    }

    public Participante atualizar(Participante participante, Long id) {
        Participante existente = this.participanteRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Projeto a ser atualizado não encontrado"));

        existente.setNome(participante.getNome());
        existente.setCpf(participante.getCpf());
        existente.setCidade(participante.getCidade());
        existente.setConta(participante.getConta());
        existente.setDataNascimento(participante.getDataNascimento());

        this.participanteRepository.save(existente);
        return existente;
    }
}
