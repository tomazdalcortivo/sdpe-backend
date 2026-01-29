package br.com.ifpr.edu.sdpe_backend.service;

import br.com.ifpr.edu.sdpe_backend.domain.Conta;
import br.com.ifpr.edu.sdpe_backend.domain.Coordenador;
import br.com.ifpr.edu.sdpe_backend.domain.Projeto;
import br.com.ifpr.edu.sdpe_backend.repository.ContaRepository;
import br.com.ifpr.edu.sdpe_backend.repository.CoordenadorRepository;
import br.com.ifpr.edu.sdpe_backend.repository.ProjetoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CoordenadorService {

    private final CoordenadorRepository coordenadorRepository;
    private final ContaRepository contaRepository;
    private final ProjetoRepository projetoRepository;

    private final Path rootLocation = Paths.get("uploads");

    public Coordenador salvar(Coordenador coordenador) {
        return this.coordenadorRepository.save(coordenador);
    }

    public Coordenador buscarPorNome(String nome) {
        return this.coordenadorRepository.findByNome(nome).orElseThrow(
                () -> new EntityNotFoundException("Coordenador não encontrado"));
    }

    public Coordenador buscarPorEmail(String email) {
        return this.coordenadorRepository.findByContaEmail(email).orElseThrow(
                () -> new EntityNotFoundException("Coordenador não encontrado"));
    }

//    public Coordenador buscarPorCpf(String cpf) {
//        return this.coordenadorRepository.findByCpf(cpf).orElseThrow(
//                () -> new EntityNotFoundException("Coordenador não encontrado"));
//    }

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

    @Transactional
    public void excluir(Long id) {
    coordenadorRepository.deleteById(id);
    }

    @Transactional
    public void desvincularCoordenadorDoProjeto(Long projetoId, Long coordenadorId) {

        Projeto projeto = projetoRepository.findById(projetoId)
                .orElseThrow(() -> new RuntimeException("Projeto não encontrado"));

        Coordenador coordenador = coordenadorRepository.findById(coordenadorId)
                .orElseThrow(() -> new RuntimeException("Coordenador não encontrado"));

        projeto.getCoordenadores().remove(coordenador);
        coordenador.getProjetos().remove(projeto);
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

    public Coordenador atualizar(Coordenador coordenador, Long id) {
        Coordenador existente = this.coordenadorRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Coordenador a ser atualizado não encontrado"));

        existente.setNome(coordenador.getNome());
        existente.setCpf(coordenador.getCpf());
        existente.setCidade(coordenador.getCidade());
        existente.setDataNascimento(coordenador.getDataNascimento());

        existente.setCargoInstituicao(coordenador.getCargoInstituicao());
        existente.setContato(coordenador.getContato());
        existente.setProjetos(coordenador.getProjetos());
        existente.setFuncao(coordenador.getFuncao());

        this.coordenadorRepository.save(existente);
        return existente;
    }
}