package br.com.ifpr.edu.sdpe_backend.service;

import br.com.ifpr.edu.sdpe_backend.domain.InstituicaoEnsino;
import br.com.ifpr.edu.sdpe_backend.exception.EntityNotFoundException;
import br.com.ifpr.edu.sdpe_backend.repository.InstituicaoEnsinoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InstituicaoEnsinoService {

    private final InstituicaoEnsinoRepository instituicaoEnsinoRepository;

    public InstituicaoEnsino salvar(InstituicaoEnsino instituicaoEnsino) {
        return instituicaoEnsinoRepository.save(instituicaoEnsino);
    }

    public InstituicaoEnsino atualizar(InstituicaoEnsino instituicaoEnsino, Long id) {
        InstituicaoEnsino existente = instituicaoEnsinoRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("instituição de ensino a ser atualizada não encontrada"));

        existente.setNome(instituicaoEnsino.getNome());
        existente.setCidade(instituicaoEnsino.getCidade());
        existente.setDescricao(instituicaoEnsino.getDescricao());

        return instituicaoEnsinoRepository.save(existente);
    }

    public List<InstituicaoEnsino> buscarTodas() {
        return this.instituicaoEnsinoRepository.findAll();
    }

    public void excluir(Long id) {
        this.instituicaoEnsinoRepository.deleteById(id);
    }

    public InstituicaoEnsino buscarPorId(Long id) {
        return this.instituicaoEnsinoRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Instituição de Ensino não encontrada"));
    }

    public InstituicaoEnsino buscarPorNome(String nome) {
        return this.instituicaoEnsinoRepository.findByNome(nome).orElseThrow(
                () -> new EntityNotFoundException("Instituição de Ensino não encontrada"));
    }

    public InstituicaoEnsino buscarPorCidade(String cidade) {
        return this.instituicaoEnsinoRepository.findByCidade(cidade).orElseThrow(
                () -> new EntityNotFoundException("Instituição de Ensino não encontrada"));
    }
}