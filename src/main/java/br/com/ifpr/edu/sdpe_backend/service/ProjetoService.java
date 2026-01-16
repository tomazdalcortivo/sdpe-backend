package br.com.ifpr.edu.sdpe_backend.service;

import br.com.ifpr.edu.sdpe_backend.domain.*;
import br.com.ifpr.edu.sdpe_backend.domain.DTO.EstatisticaDTO;
import br.com.ifpr.edu.sdpe_backend.exception.EntityNotFoundException;
import br.com.ifpr.edu.sdpe_backend.repository.InstituicaoEnsinoRepository;
import br.com.ifpr.edu.sdpe_backend.repository.ProjetoRepository;
import br.com.ifpr.edu.sdpe_backend.repository.VisualizacoesRepository;
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

    private final VisualizacoesRepository visualizacoesRepository;

    private final Path rootLocation = Paths.get("uploads");

    public Projeto salvar(Projeto projeto, MultipartFile arquivo, String emailCoordenador) throws IOException {

        projeto.setStatus(true);

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

    private void tratarInstituicao(Projeto projeto) {
        if (projeto.getInstituicaoEnsino() != null) {
            InstituicaoEnsino input = projeto.getInstituicaoEnsino();
            input.getProjetos().add(projeto);

            if (input.getNome() != null && !input.getNome().trim().isEmpty()) {

                Optional<InstituicaoEnsino> busca;

                if (input.getCidade() != null && !input.getCidade().trim().isEmpty()) {
                    busca = instituicaoEnsinoRepository.findByNomeAndCidade(input.getNome(), input.getCidade());
                } else {
                    busca = instituicaoEnsinoRepository.findByNome(input.getNome());
                }

                InstituicaoEnsino instituicaoFinal;


                if (busca.isPresent()) {
                    // Se já existe, usamos a do banco (ignorando a descrição nova para não sobrescrever dados antigos sem querer)
                    instituicaoFinal = busca.get();
                } else {
                    instituicaoFinal = instituicaoEnsinoRepository.save(input);
                }

                projeto.setInstituicaoEnsino(instituicaoFinal);
            }
        }
    }

    public List<Projeto> buscarPorCoordenador(Long idCoordenador) {
        return projetoRepository.findByCoordenadores_Id(idCoordenador);
    }

    public List<Projeto> buscarPorParticipante(Long idParticipante) {
        return projetoRepository.findByParticipantes_Id(idParticipante);
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
        Projeto projeto = buscarPorId(id);

        if (projeto.getImagemPath() != null && !projeto.getImagemPath().trim().isEmpty())
            deletarArquivoFisico(projeto.getImagemPath());

        if (projeto.getDocumentoPath() != null && !projeto.getDocumentoPath().trim().isEmpty())
            deletarArquivoFisico(projeto.getDocumentoPath());

        this.projetoRepository.deleteById(id);
    }

    private void deletarArquivoFisico(String nomeArquivo) {
        try {
            Path arquivo = rootLocation.resolve(nomeArquivo);

            Files.deleteIfExists(arquivo);
        } catch (IOException e) {
            System.err.println("Erro ao deletar arquivo físico (" + nomeArquivo + "): " + e.getMessage());
        }
    }

    public Projeto buscarPorId(Long id) {
        Projeto projeto = this.projetoRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Projeto não encontrado"));

        Visualizacao visualizacao = Visualizacao.builder()
                .projeto(projeto)
                .build();

        visualizacoesRepository.save(visualizacao);

        return projeto;
    }

    public List<EstatisticaDTO> getEstatisticasVisualizacoes() {
        List<Object[]> dados = visualizacoesRepository.countVisualizacoesPorMesNoAnoAtual();
        List<EstatisticaDTO> estatisticas = new ArrayList<>();

        String[] meses = {"Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

        for (Object[] d : dados) {
            int mesIndex = (int) d[0] - 1;
            Long qtd = (Long) d[1];
            estatisticas.add(new EstatisticaDTO(meses[mesIndex], qtd));
        }
        return estatisticas;
    }

    public List<EstatisticaDTO> getEstatisticasPorArea() {

        List<Object[]> dados = projetoRepository.contProjetosPorArea();
        List<EstatisticaDTO> estatisticas = new ArrayList<>();

        for (Object[] d : dados) {
            String area = (String) d[0];
            Long qtd = (Long) d[1];
            estatisticas.add(new EstatisticaDTO(area, qtd));
        }
        return estatisticas;
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