package br.com.ifpr.edu.sdpe_backend.service;

import br.com.ifpr.edu.sdpe_backend.domain.Projeto;
import br.com.ifpr.edu.sdpe_backend.exception.EntityNotFoundException;
import br.com.ifpr.edu.sdpe_backend.repository.ProjetoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjetoService {

    private final ProjetoRepository projetoRepository;

    public Projeto salvar(Projeto projeto) {
        return this.projetoRepository.save(projeto);
    }

    public List<Projeto> buscarTodos() {
        return this.projetoRepository.findAll();
    }

    public Projeto atualizar(Projeto projeto, Long id) {
        Projeto existente = this.projetoRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Projeto a ser atualizado não encontrado"));

        existente.setNome(projeto.getNome());
        existente.setDescricao(projeto.getDescricao());
        existente.setCoordenador(projeto.getCoordenador());
        existente.setCargaHoraria(projeto.getCargaHoraria());
        existente.setFormato(projeto.getFormato());

        Projeto.builder()
                .nome(projeto.getNome())
                .descricao(projeto.getDescricao())
                .coordenador(projeto.getCoordenador())
                .cargaHoraria(projeto.getCargaHoraria())
                .build();

        this.projetoRepository.save(existente);
        return existente;
    }

    public void excluir(Long id) {
        Projeto projeto = this.buscarPorId(id);
        this.projetoRepository.delete(projeto);
    }

    public Projeto buscarPorId(Long id) {
        return this.projetoRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Projeto não encontrado"));
    }
}
