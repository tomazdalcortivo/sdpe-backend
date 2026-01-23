package br.com.ifpr.edu.sdpe_backend.controller;


import br.com.ifpr.edu.sdpe_backend.domain.Contato;
import br.com.ifpr.edu.sdpe_backend.domain.DTO.ContatoPublicoDTO;
import br.com.ifpr.edu.sdpe_backend.service.AltchaService;
import br.com.ifpr.edu.sdpe_backend.service.ContatoService;
import br.com.ifpr.edu.sdpe_backend.service.ProjetoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.altcha.altcha.Altcha.Challenge;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/contatos")
public class ContatoController {

    private final ContatoService contatoService;
    private final AltchaService altchaService;
    private final ProjetoService projetoService;


    @GetMapping("/challenge")
    public ResponseEntity<Challenge> getChallenge() {
        return ResponseEntity.ok(altchaService.createChallenge());
    }

    @PostMapping
    public ResponseEntity<?> enviarContato(@RequestBody @Valid ContatoPublicoDTO dados) {
        boolean isHuman = altchaService.verifySolution(dados.altcha());

        if (!isHuman) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(java.util.Collections.singletonMap("error", "Verificação de segurança falhou (Captcha inválido)."));
        }

        Contato novoContato = new Contato();
        novoContato.setNome(dados.nome());
        novoContato.setEmail(dados.email());
        novoContato.setMensagem(dados.mensagem());
        novoContato.setTipoContato(dados.tipoContato());

        if (dados.projetoId() != null) projetoService.buscarPorId(dados.projetoId());


        contatoService.salvar(novoContato);

        return ResponseEntity.status(HttpStatus.CREATED).body(novoContato);
    }
}