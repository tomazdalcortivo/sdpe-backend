package br.com.ifpr.edu.sdpe_backend.service;

import br.com.ifpr.edu.sdpe_backend.domain.Documento;
import br.com.ifpr.edu.sdpe_backend.repository.DocumentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class DocumentoService {

    private final DocumentoRepository repository;

    public void excluir(Long id) {
        Documento doc = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Documento não encontrado"));
        try {
            String nomeArquivo = doc.getUrl().substring(doc.getUrl().lastIndexOf("/") + 1);
            Path caminho = Paths.get("uploads").resolve(nomeArquivo);
            Files.deleteIfExists(caminho);
        } catch (Exception e) {
            System.out.println("Erro ao apagar arquivo físico, mas prosseguindo: " + e.getMessage());
        }
        repository.delete(doc);
    }
}