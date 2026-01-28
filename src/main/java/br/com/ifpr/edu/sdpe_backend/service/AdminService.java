package br.com.ifpr.edu.sdpe_backend.service;

import br.com.ifpr.edu.sdpe_backend.domain.Conta;
import br.com.ifpr.edu.sdpe_backend.domain.Contato;
import br.com.ifpr.edu.sdpe_backend.domain.Participante;
import br.com.ifpr.edu.sdpe_backend.domain.Projeto;
import br.com.ifpr.edu.sdpe_backend.domain.enums.TipoPerfil;
import br.com.ifpr.edu.sdpe_backend.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final ContaRepository contaRepository;
    private final ParticipanteRepository participanteRepository;
    private final ProjetoRepository projetoRepository;
    private final ProjetoService projetoService;
    private final ContatoRepository contatoRepository;
    private final VisualizacaoRepository visualizacaoRepository;
    private final EmailService emailService;


    public List<Participante> listarTodosUsuarios() {
        return participanteRepository.findAll();
    }

    public List<Participante> listarPendentes() {
        return participanteRepository.findPendentesDeAprovacao();
    }

    public List<Projeto> listarTodosProjetos() {
        return projetoRepository.findAll();
    }

    public List<Contato> listarTodosContatos() {
        return contatoRepository.findAll(Sort.by(Sort.Direction.DESC, "dataEnvio"));
    }

    public void alterarStatusConta(Long id, Boolean ativo) {
        Optional<Conta> contaOpt = contaRepository.findById(id);
        if (contaOpt.isPresent()) {
            Conta conta = contaOpt.get();
            conta.setAtivo(ativo);
            contaRepository.save(conta);
        } else {
            throw new RuntimeException("Conta não encontrada");
        }
    }

    @Transactional
    public void excluirConta(Long id) {
        Conta conta = contaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada"));

        Participante participante = conta.getParticipante();

        if (participante != null) {
            conta.setParticipante(null);
            participante.setConta(null);
            contaRepository.save(conta);

            if (conta.getPerfil() == TipoPerfil.COORDENADOR || conta.getPerfil() == TipoPerfil.ADMIN) {
                List<Projeto> projetos = projetoRepository.findByCoordenadoresId(participante.getId());
                for (Projeto p : projetos) {
                    excluirDependenciasProjeto(p.getId());
                }
                projetoRepository.deleteAll(projetos);
            }
            participanteRepository.delete(participante);
        }
        contaRepository.delete(conta);
    }

    public List<Projeto> listarProjetosPendentes() {
        return this.projetoRepository.findByAtivo(false);
    }

    public void atualizarStatusProjeto(Long idProjeto, Boolean ativo) {
        Projeto projeto = this.projetoService.buscarPorId(idProjeto);
        if (ativo) projeto.setMotivoRejeicao(null);
        projeto.setAtivo(ativo);
        this.projetoRepository.save(projeto);
    }

    public void rejeitarProjeto(Long id, String motivo) {
        Projeto projeto = projetoService.buscarPorId(id);

        projeto.setMotivoRejeicao(motivo);
        projeto.setAtivo(false);

        projetoRepository.save(projeto);
    }

    @Transactional
    public void excluirProjeto(Long id) {
        if (!projetoRepository.existsById(id)) {
            throw new RuntimeException("Projeto não encontrado");
        }
        excluirDependenciasProjeto(id);
        projetoRepository.deleteById(id);
    }

    public void responderContato(Long idContato, String mensagem) {
        Contato contato = contatoRepository.findById(idContato)
                .orElseThrow(() -> new RuntimeException("Contato não encontrado"));

        emailService.enviarRespostaSuporte(contato.getEmail(), contato.getNome(), mensagem);
    }

    private void excluirDependenciasProjeto(Long projetoId) {
        List<Contato> contatos = contatoRepository.findByProjetoId(projetoId);
        if (!contatos.isEmpty()) contatoRepository.deleteAll(contatos);
        contatoRepository.deleteByProjetoId(projetoId);
        visualizacaoRepository.deleteByProjetoId(projetoId);
    }
}