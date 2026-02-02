package br.com.ifpr.edu.sdpe_backend.service;

import br.com.ifpr.edu.sdpe_backend.domain.Contato;
import br.com.ifpr.edu.sdpe_backend.domain.Projeto;
import br.com.ifpr.edu.sdpe_backend.repository.ContatoRepository;
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

    public Contato salvar(Contato contato) {
        return contatoRepository.save(contato);
    }

    public Contato atualizar(Contato contato, Long id) {
        Contato existente = this.contatoRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Projeto a ser atualizado não encontrado"));

        existente.setMensagem(contato.getMensagem());
        existente.setTipoContato(contato.getTipoContato());
        existente.setDataEnvio(contato.getDataEnvio());
        existente.setProjeto(contato.getProjeto());

        return this.contatoRepository.save(existente);
    }

    public Page<Contato> buscarTodos(int numPag, int tamPag) {
        Pageable pageable = PageRequest.of(numPag, tamPag);
        return this.contatoRepository.findAll(pageable);
    }

    public List<Contato> buscarPorProjeto(Long projetoId) {
        return this.contatoRepository.findByProjetoId(projetoId);
    }

    public void excluir(Long id) {
        this.contatoRepository.deleteById(id);
    }

    public Contato buscarPorId(Long id) {
        return this.contatoRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("contato não encontrado"));
    }
}
