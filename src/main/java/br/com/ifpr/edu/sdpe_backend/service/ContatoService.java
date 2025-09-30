package br.com.ifpr.edu.sdpe_backend.service;

import br.com.ifpr.edu.sdpe_backend.domain.Contato;
import br.com.ifpr.edu.sdpe_backend.repository.ContatoRepository;
import br.com.ifpr.edu.sdpe_backend.exception.EntityNotFoundException;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    }

    public void excluir(Long id) {
        Contato contato = this.buscarPorId(id);
        contatoRepository.delete(contato);
    }

    public Contato buscarPorId(Long id) {
        return this.contatoRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("contato não encontrado"));
    }
}
