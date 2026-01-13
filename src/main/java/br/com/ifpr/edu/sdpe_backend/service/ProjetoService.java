package br.com.ifpr.edu.sdpe_backend.service;

import br.com.ifpr.edu.sdpe_backend.domain.Contato;
import br.com.ifpr.edu.sdpe_backend.domain.Coordenador;
import br.com.ifpr.edu.sdpe_backend.domain.InstituicaoEnsino; // Import adicionado
import br.com.ifpr.edu.sdpe_backend.domain.Participante;
import br.com.ifpr.edu.sdpe_backend.domain.Projeto;
import br.com.ifpr.edu.sdpe_backend.exception.EntityNotFoundException;
import br.com.ifpr.edu.sdpe_backend.repository.InstituicaoEnsinoRepository; // Import adicionado
import br.com.ifpr.edu.sdpe_backend.repository.ProjetoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjetoService {

    private final ProjetoRepository projetoRepository;

    private final ParticipanteService participanteService;

    private final ContatoService contatoService;

    private final InstituicaoEnsinoRepository instituicaoEnsinoRepository;

    public Projeto salvar(Projeto projeto) {

        if (projeto.getInstituicaoEnsino() != null) {
            InstituicaoEnsino instituicaoInput = projeto.getInstituicaoEnsino();

            if (instituicaoInput.getNome() != null && !instituicaoInput.getNome().isEmpty()) {
                InstituicaoEnsino instituicaoBanco = instituicaoEnsinoRepository
                        .findByNome(instituicaoInput.getNome())
                        .orElseGet(() -> instituicaoEnsinoRepository.save(instituicaoInput)); // Salva se não existir

                projeto.setInstituicaoEnsino(instituicaoBanco);
            }
        }

        return this.projetoRepository.save(projeto);
    }

    public Page<Projeto> buscarTodos(int numPag, int tamPag) {
        Pageable pageable = PageRequest.of(numPag, tamPag);
        return this.projetoRepository.findAll(pageable);
    }

    public Projeto atualizar(Projeto projeto, Long id) {
        Projeto existente = this.projetoRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Projeto a ser atualizado não encontrado"));

        existente.setNome(projeto.getNome());
        existente.setDescricao(projeto.getDescricao());
        existente.setCoordenadores(projeto.getCoordenadores());
        existente.setParticipantes(projeto.getParticipantes());
        existente.setCargaHoraria(projeto.getCargaHoraria());
        existente.setFormato(projeto.getFormato());


        if (projeto.getInstituicaoEnsino() != null) {

            InstituicaoEnsino inst = instituicaoEnsinoRepository.findByNome(projeto.getInstituicaoEnsino().getNome())
                    .orElseGet(() -> instituicaoEnsinoRepository.save(projeto.getInstituicaoEnsino()));
            existente.setInstituicaoEnsino(inst);
        }

        this.projetoRepository.save(existente);
        return existente;
    }

    public void excluir(Long id) {
        this.projetoRepository.deleteById(id);
    }

    public Projeto buscarPorId(Long id) {
        return this.projetoRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Projeto não encontrado"));
    }

    public List<Projeto> buscarPorPeriodo(Date dataInicio, Date dataFim) {
        return this.projetoRepository.findByDataInicioGreaterThanEqualAndDataFimLessThanEqual(dataInicio, dataFim);
    }

    public List<Coordenador> listarCoordenadores(Long idProjeto) {
        Projeto projeto = buscarPorId(idProjeto);
        return projeto.getCoordenadores();
    }

    public List<Participante> listarParticipantes(Long idProjeto) {
        Projeto projeto = buscarPorId(idProjeto);
        return projeto.getParticipantes();
    }

    public List<Contato> listarContatos(Long idProjeto) {
        Projeto projeto = buscarPorId(idProjeto);
        return projeto.getContatos();
    }

    public void adicionarParticipante(Long idProjeto, Long idParticipante) {
        Projeto projeto = buscarPorId(idProjeto);

        Participante participante = this.participanteService.buscarPorId(idParticipante);

        if (!projeto.getParticipantes().contains(participante)) {
            projeto.getParticipantes().add(participante);

            participante.getProjetos().add(projeto);
            this.projetoRepository.save(projeto);
        }
    }

    public void excluirParticipante(Long idProjeto, Long idParticipante) {
        Projeto projeto = buscarPorId(idProjeto);

        Participante participante = this.participanteService.buscarPorId(idParticipante);

        if (projeto.getParticipantes().contains(participante)) {
            projeto.getParticipantes().remove(participante);

            participante.getProjetos().remove(projeto);
            this.projetoRepository.save(projeto);
        }
    }

    public Contato adicionarFeedback(Long idProjeto, Contato contato) {
        Projeto projeto = buscarPorId(idProjeto);
        contato.setProjeto(projeto);
        return contatoService.salvar(contato);
    }

    public void removerFeedback(Long idFeedback) {
        contatoService.excluir(idFeedback);
    }
}