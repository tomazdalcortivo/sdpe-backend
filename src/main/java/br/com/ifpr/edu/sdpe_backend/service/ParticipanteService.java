package br.com.ifpr.edu.sdpe_backend.service;

import br.com.ifpr.edu.sdpe_backend.domain.Conta;
import br.com.ifpr.edu.sdpe_backend.domain.DTO.ParticipanteUpdateDTO;
import br.com.ifpr.edu.sdpe_backend.domain.Participante;
import br.com.ifpr.edu.sdpe_backend.domain.Projeto;
import br.com.ifpr.edu.sdpe_backend.exception.EntityNotFoundException;
import br.com.ifpr.edu.sdpe_backend.repository.ContaRepository;
import br.com.ifpr.edu.sdpe_backend.repository.ParticipanteRepository;
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
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ParticipanteService {

    private final ParticipanteRepository participanteRepository;

    private final ContaRepository contaRepository;

    private final Path rootLocation = Paths.get("uploads");

    public Participante salvar(Participante participante) {
        return this.participanteRepository.save(participante);
    }

    public Participante buscarPorId(Long id) {
        return this.participanteRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Participante não encontrado"));
    }

    public List<Participante> buscarPorNome(String nome) {
        return this.participanteRepository.findByNomeContainingIgnoreCase(nome);
    }

    public Participante buscarPorEmail(String email) {
        return this.participanteRepository.findByContaEmail(email).orElseThrow(
                () -> new EntityNotFoundException("Participante não encontrado"));
    }

    public Participante buscarPorCpf(String cpf) {
        return this.participanteRepository.findByCpf(cpf).orElseThrow(
                () -> new EntityNotFoundException("Participante não encontrado"));
    }

    public Page<Participante> listarPorProjeto(Projeto projeto, int numPag, int tamPag) {
        Pageable pageable = PageRequest.of(numPag, tamPag);
        return this.participanteRepository.findByProjetos(projeto, pageable);
    }

    public Participante uploadFotoPerfil(Long id, MultipartFile arquivo) throws IOException {
        Participante participante = this.buscarPorId(id);

        if (!Files.exists(rootLocation)) Files.createDirectory(rootLocation);

        String filename = "perfil-" + id + "-" + UUID.randomUUID().toString() + ".jpg";

        Path destinationFile = rootLocation.resolve(filename);
        Files.copy(arquivo.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);

        String fileUrl = "http://localhost:8080/imagens/" + filename;

        participante.setFotoPerfil(fileUrl);
        return this.participanteRepository.save(participante);
    }

    @Transactional
    public void excluir(Long id) {
        Participante participante = buscarPorId(id);
        Conta conta = participante.getConta();

        this.participanteRepository.delete(participante);

        if (conta != null) this.contaRepository.delete(conta);

    }

    public Participante atualizar(ParticipanteUpdateDTO dadosNovos, Long id) {
        Participante existente = this.participanteRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Participante não encontrado"));

        if (dadosNovos.nome() != null && !dadosNovos.nome().isBlank()) existente.setNome(dadosNovos.nome());
        if (dadosNovos.cidade() != null) existente.setCidade(dadosNovos.cidade());
        if (dadosNovos.telefone() != null) existente.setTelefone(dadosNovos.telefone());
        if (dadosNovos.resumo() != null) existente.setResumo(dadosNovos.resumo());

        return this.participanteRepository.save(existente);
    }

}
