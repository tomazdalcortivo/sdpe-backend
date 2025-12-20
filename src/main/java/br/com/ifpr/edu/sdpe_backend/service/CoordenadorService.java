package br.com.ifpr.edu.sdpe_backend.service;

import br.com.ifpr.edu.sdpe_backend.domain.Coordenador;
import br.com.ifpr.edu.sdpe_backend.domain.Projeto;
import br.com.ifpr.edu.sdpe_backend.exception.EntityNotFoundException;
import br.com.ifpr.edu.sdpe_backend.repository.CoordenadorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
public class CoordenadorService {

    private final CoordenadorRepository coordenadorRepository;

    public Coordenador salvar(Coordenador coordenador) {
        return this.coordenadorRepository.save(coordenador);
    }

    public Coordenador buscarPorNome(String nome) {
        return this.coordenadorRepository.findByNome(nome).orElseThrow(
                () -> new EntityNotFoundException("Coordenador não encontrado"));
    }

    public Coordenador buscarPorCpf(String cpf) {
        return this.coordenadorRepository.findByCpf(cpf).orElseThrow(
                () -> new EntityNotFoundException("Coordenador não encontrado"));
    }

    public Coordenador buscarPorContato(String contato) {
        return this.coordenadorRepository.findByContato(contato).orElseThrow(
                () -> new EntityNotFoundException("Coordenador não encontrado"));
    }

    public Coordenador buscarPorId(Long id) {
        return this.coordenadorRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Coordenador não encontrado"));
    }

    public Page<Projeto> listarProjetos(Coordenador coordenador, int numPag, int tamPag) {
        Pageable pageable = PageRequest.of(numPag, tamPag);
        return this.coordenadorRepository.findByProjetos(coordenador, pageable);
    }


    public void excluir(Long id) {
        this.coordenadorRepository.deleteById(id);
    }

    public Coordenador atualizar(Coordenador coordenador, Long id) {
        Coordenador existente = this.coordenadorRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Coordenador a ser atualizado não encontrado"));

        existente.setNome(coordenador.getNome());
        existente.setCpf(coordenador.getCpf());
        existente.setCidade(coordenador.getCidade());
        existente.setDataNascimento(coordenador.getDataNascimento());
        // Se você não quiser mudar a conta, não a defina.
        // existente.setConta(coordenador.getConta());

        existente.setCargoInstituicao(coordenador.getCargoInstituicao());
        existente.setContato(coordenador.getContato());
        existente.setProjeto(coordenador.getProjeto());
        existente.setFuncao(coordenador.getFuncao());

        this.coordenadorRepository.save(existente);
        return existente;
    }
}