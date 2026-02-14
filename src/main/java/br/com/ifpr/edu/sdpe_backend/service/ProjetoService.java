package br.com.ifpr.edu.sdpe_backend.service;

import br.com.ifpr.edu.sdpe_backend.domain.*;
import br.com.ifpr.edu.sdpe_backend.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ProjetoService {

    private final ProjetoRepository projetoRepository;

    private final ParticipanteService participanteService;

    private final ContatoService contatoService;

    private final InstituicaoEnsinoRepository instituicaoEnsinoRepository;

    private final CoordenadorService coordenadorService;

    private final VisualizacaoRepository visualizacaoRepository;

    private final ContatoRepository contatoRepository;

    private final DocumentoRepository documentoRepository;

    private final Path rootLocation = Paths.get("uploads");

    public Projeto salvar(Projeto projeto) {
        return projetoRepository.save(projeto);
    }

    public Projeto salvar(Projeto projeto, MultipartFile arquivo, String emailCoordenador) throws IOException {

        projeto.setAtivo(false);

        if (projeto.getDataInicio() != null && projeto.getDataFim() != null) {
            if (projeto.getDataFim().before(projeto.getDataInicio())) {
                throw new IllegalArgumentException("A data de fim não pode ser anterior à data de início.");
            }
        }

        if (arquivo != null && !arquivo.isEmpty()) {
            if (!Files.exists(rootLocation)) Files.createDirectories(rootLocation);

            String filename = "doc-projeto-" + UUID.randomUUID() + ".pdf";
            Path destinationFile = rootLocation.resolve(filename);

            Files.copy(arquivo.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);

            String documentoPath = "http://localhost:8080/documentos/" + filename;

            projeto.setDocumentoPath(documentoPath);
        }
        if (emailCoordenador != null && !emailCoordenador.isEmpty()) {
            try {
                Coordenador coordenador = coordenadorService.buscarPorEmail(emailCoordenador);
                if (!projeto.getCoordenadores().contains(coordenador)) {
                    projeto.getCoordenadores().add(coordenador);
                }
            } catch (Exception e) {
                System.err.println("Erro ao vincular coordenador: " + e.getMessage());
            }
        }

        tratarInstituicao(projeto);
        return this.projetoRepository.save(projeto);
    }

    private String salvarArquivo(MultipartFile file) throws IOException {
        Path uploadPath = Paths.get("uploads");
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String nomeArquivo = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(nomeArquivo);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return "http://localhost:8080/documentos/" + nomeArquivo;
    }

    private void tratarInstituicao(Projeto projeto) {
        if (projeto.getInstituicaoEnsino() != null) {
            InstituicaoEnsino input = projeto.getInstituicaoEnsino();

            if (input.getNome() != null) input.setNome(input.getNome().trim());
            if (input.getCidade() != null) input.setCidade(input.getCidade().trim());
            if (input.getEstado() != null) input.setEstado(input.getEstado().trim());

            if (input.getNome() != null && !input.getNome().isEmpty()) {
                Optional<InstituicaoEnsino> busca;

                if (input.getCidade() != null && !input.getCidade().isEmpty() &&
                        input.getEstado() != null && !input.getEstado().isEmpty()) {

                    busca = instituicaoEnsinoRepository.findByNomeAndCidadeAndEstado(
                            input.getNome(),
                            input.getCidade(),
                            input.getEstado()
                    );
                } else {
                    busca = instituicaoEnsinoRepository.findByNome(input.getNome());
                }

                if (busca.isPresent()) {
                    projeto.setInstituicaoEnsino(busca.get());
                } else {
                    projeto.setInstituicaoEnsino(instituicaoEnsinoRepository.save(input));
                }
            }
        }
    }

    public List<Projeto> buscarPorCoordenador(Long idCoordenador) {
        return projetoRepository.findByCoordenadoresId(idCoordenador);
    }

    public List<Projeto> buscarPorParticipante(Long idParticipante) {
        return projetoRepository.findByParticipantesId(idParticipante);
    }

    public Page<Projeto> buscarTodos(int numPag, int tamPag) {
        Pageable pageable = PageRequest.of(numPag, tamPag);
        return this.projetoRepository.findByAtivo(true, pageable);
    }

    public Projeto atualizar(Projeto projeto, Long id, MultipartFile imagem, List<MultipartFile> docs) throws IOException {
        Projeto existente = this.projetoRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Projeto a ser atualizado não encontrado"));

        existente.setNome(projeto.getNome());
        existente.setDescricao(projeto.getDescricao());
        existente.setArea(projeto.getArea());
        existente.setDataInicio(projeto.getDataInicio());
        existente.setDataFim(projeto.getDataFim());
        existente.setCargaHoraria(projeto.getCargaHoraria());
        existente.setFormato(projeto.getFormato());

        existente.setAtivo(false);
        existente.setMotivoRejeicao(null);

        if (projeto.getRedesSociais() != null) existente.setRedesSociais(projeto.getRedesSociais());


        if (projeto.getInstituicaoEnsino() != null) {
            InstituicaoEnsino inst = instituicaoEnsinoRepository.findByNome(projeto.getInstituicaoEnsino().getNome())
                    .orElseGet(() -> instituicaoEnsinoRepository.save(projeto.getInstituicaoEnsino()));
            existente.setInstituicaoEnsino(inst);
        }

        if (imagem != null && !imagem.isEmpty()) {
            if (!Files.exists(rootLocation)) Files.createDirectories(rootLocation);

            if (existente.getImagemPath() != null && !existente.getImagemPath().trim().isEmpty()) {
                deletarArquivoFisico(existente.getImagemPath());
            }

            String filename = "img-projeto-" + UUID.randomUUID() + "-" + imagem.getOriginalFilename();
            Path destinationFile = rootLocation.resolve(filename);

            Files.copy(imagem.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);

            existente.setImagemPath(destinationFile.toString());
        }

        if (docs != null && !docs.isEmpty()) {
            for (MultipartFile file : docs) {
                try {
                    String url = salvarArquivo(file);

                    Documento doc = Documento.builder()
                            .nome(file.getOriginalFilename())
                            .url(url)
                            .projeto(existente)
                            .build();

                    if (existente.getDocumentos() == null) existente.setDocumentos(new ArrayList<>());
                    existente.getDocumentos().add(doc);
                } catch (IOException e) {
                    throw new RuntimeException("Erro ao salvar documento", e);
                }
            }
        }

        return this.projetoRepository.save(existente);
    }

    @Transactional
    public void excluir(Long id) {
        Projeto projeto = this.buscarPorId(id);

        visualizacaoRepository.deleteByProjetoId(id);

        List<Contato> contatos = contatoRepository.findByProjetoId(id);
        if (!contatos.isEmpty()) contatoRepository.deleteAll(contatos);

        deletarArquivoFisico(projeto.getImagemPath());
        deletarArquivoFisico(projeto.getDocumentoPath());

        this.projetoRepository.deleteById(id);
    }

    private void deletarArquivoFisico(String caminhoOuUrl) {
        if (caminhoOuUrl != null && !caminhoOuUrl.isBlank()) {
            try {
                String nomeArquivo = caminhoOuUrl;
                if (caminhoOuUrl.contains("/")) {
                    nomeArquivo = caminhoOuUrl.substring(caminhoOuUrl.lastIndexOf("/") + 1);
                }

                Path arquivo = rootLocation.resolve(nomeArquivo);
                Files.deleteIfExists(arquivo);
            } catch (Exception e) {
                System.err.println("Erro ao deletar arquivo físico (" + caminhoOuUrl + "): " + e.getMessage());
            }
        }
    }

    public Projeto buscarPorId(Long id) {
        return this.projetoRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Projeto não encontrado"));
    }

    public void registrarVisualizacao(Long id) {
        Projeto projeto = buscarPorId(id);

        Visualizacao visualizacao = Visualizacao.builder()
                .projeto(projeto)
                .build();

        visualizacaoRepository.save(visualizacao);
    }

    public List<Projeto> buscarPorPeriodo(Date dataInicio, Date dataFim) {
        return this.projetoRepository.findByDataInicioGreaterThanEqualAndDataFimLessThanEqual(dataInicio, dataFim);
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

    public void removerParticipante(Long idProjeto, Long idParticipante) {
        Projeto projeto = buscarPorId(idProjeto);

        Participante participante = this.participanteService.buscarPorId(idParticipante);

        if (projeto.getParticipantes().contains(participante)) {
            projeto.getParticipantes().remove(participante);

            participante.getProjetos().remove(projeto);
            this.projetoRepository.save(projeto);
        }
    }

    public void adicionarCoordenador(Long idProjeto, Long idCoordenador) {
        Projeto projeto = buscarPorId(idProjeto);

        Coordenador coordenador = coordenadorService.buscarPorId(idCoordenador);

        if (!projeto.getCoordenadores().contains(coordenador)) {
            projeto.getCoordenadores().add(coordenador);
            this.projetoRepository.save(projeto);
        }
    }

    public void removerCoordenador(Long idProjeto, Long idCoordenador) {
        Projeto projeto = buscarPorId(idProjeto);
        Coordenador coordenador = coordenadorService.buscarPorId(idCoordenador);

        if (projeto.getCoordenadores().contains(coordenador)) {
            projeto.getCoordenadores().remove(coordenador);
            this.projetoRepository.save(projeto);
        }
    }

    public List<Contato> listarFeedbacks(Long idProjeto) {
        buscarPorId(idProjeto);
        return contatoService.buscarPorProjeto(idProjeto);
    }

    public Contato adicionarFeedback(Long idProjeto, Contato contato) {
        Projeto projeto = buscarPorId(idProjeto);

        contato.setProjeto(projeto);

        return contatoService.salvar(contato);
    }

    @Transactional
    public Contato editarFeedback(Long idProjeto, Long idFeedback, String novaMensagem, String emailLogado) {
        buscarPorId(idProjeto);
        Contato feedback = contatoRepository.findById(idFeedback)
                .orElseThrow(() -> new EntityNotFoundException("Feedback não encontrado"));

        if (!feedback.getEmail().equals(emailLogado)) {
            throw new IllegalArgumentException("Você não tem permissão para editar este feedback.");
        }

        feedback.setMensagem(novaMensagem);
        return contatoRepository.save(feedback);
    }

    @Transactional
    public void removerFeedback(Long idProjeto, Long idFeedback, String emailLogado) {
        Projeto projeto = buscarPorId(idProjeto);

        Contato feedback = contatoRepository.findById(idFeedback)
                .orElseThrow(() -> new EntityNotFoundException("Feedback não encontrado"));

        boolean Autor = feedback.getEmail().equals(emailLogado);

        boolean Coordenador = projeto.getCoordenadores().stream()
                .anyMatch(c -> c.getConta().getEmail().equals(emailLogado));

        if (Autor || Coordenador) {
            contatoService.excluir(idFeedback);
        } else {
            throw new IllegalArgumentException("Sem permissão para remover este feedback.");
        }
    }

//    public void removerFeedback(Long idFeedback) {
//        contatoService.excluir(idFeedback);
//    }
}