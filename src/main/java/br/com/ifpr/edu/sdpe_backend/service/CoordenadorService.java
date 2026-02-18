package br.com.ifpr.edu.sdpe_backend.service;

import br.com.ifpr.edu.sdpe_backend.domain.Conta;
import br.com.ifpr.edu.sdpe_backend.domain.Coordenador;
import br.com.ifpr.edu.sdpe_backend.domain.Post;
import br.com.ifpr.edu.sdpe_backend.domain.Projeto;
import br.com.ifpr.edu.sdpe_backend.repository.ContaRepository;
import br.com.ifpr.edu.sdpe_backend.repository.CoordenadorRepository;
import br.com.ifpr.edu.sdpe_backend.repository.PostRepository;
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
import java.util.List;

@Service
@RequiredArgsConstructor
public class CoordenadorService {

    private final CoordenadorRepository coordenadorRepository;
    private final ContaRepository contaRepository;
    private final ProjetoRepository projetoRepository;
    private final PostRepository postRepository;

    private final Path rootLocation = Paths.get("uploads");

    public Coordenador salvar(Coordenador coordenador) {
        return this.coordenadorRepository.save(coordenador);
    }

    public List<Coordenador> buscarPorNome(String nome) {
        return coordenadorRepository.findByNomeContainingIgnoreCase(nome);
    }

    public Coordenador buscarPorEmail(String email) {
        return this.coordenadorRepository.findByContaEmail(email).orElseThrow(
                () -> new EntityNotFoundException("Coordenador não encontrado"));
    }

    public Coordenador buscarPorId(Long id) {
        return this.coordenadorRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Coordenador não encontrado"));
    }

    public Page<Projeto> listarProjetos(Coordenador coordenador, int numPag, int tamPag) {
        Pageable pageable = PageRequest.of(numPag, tamPag);
        return this.coordenadorRepository.findByMeusProjetosCriados(coordenador, pageable);
    }

    @Transactional
    public void excluir(Long id) {
        List<Projeto> projetosVinculados = projetoRepository.findByCoordenadoresId(id);

        for (Projeto projeto : projetosVinculados) {
            boolean removed = projeto.getCoordenadores().removeIf(c -> c.getId().equals(id));

            if (removed) projetoRepository.save(projeto);

        }

        try {
            List<Post> posts = postRepository.findByAutorId(id);
            if (!posts.isEmpty()) postRepository.deleteAll(posts);
        } catch (Exception e) {
            
        }

        Coordenador coordenador = buscarPorId(id);

        deletarArquivoFisico(coordenador.getFotoPerfil());
        deletarArquivoFisico(coordenador.getDocumentoUrl());

        Conta conta = coordenador.getConta();

        if (conta != null) {
            conta.setParticipante(null);
            contaRepository.save(conta);
        }

        coordenadorRepository.deleteById(id);

        if (conta != null) contaRepository.delete(conta);

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
                System.err.println("Erro ao deletar arquivo físico: " + e.getMessage());
            }
        }
    }

    public Coordenador atualizar(Coordenador coordenador, Long id) {
        Coordenador existente = this.coordenadorRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Coordenador a ser atualizado não encontrado"));

        existente.setNome(coordenador.getNome());
        existente.setCidade(coordenador.getCidade());
        existente.setEstado(coordenador.getEstado());
        existente.setTelefone(coordenador.getTelefone());
        existente.setResumo(coordenador.getResumo());

        this.coordenadorRepository.save(existente);
        return existente;
    }
}