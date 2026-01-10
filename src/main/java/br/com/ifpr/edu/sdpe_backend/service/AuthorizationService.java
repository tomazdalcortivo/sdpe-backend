package br.com.ifpr.edu.sdpe_backend.service;

import br.com.ifpr.edu.sdpe_backend.domain.Conta;
import br.com.ifpr.edu.sdpe_backend.domain.Coordenador;
import br.com.ifpr.edu.sdpe_backend.domain.DTO.RegisterDTO;
import br.com.ifpr.edu.sdpe_backend.domain.Participante;
import br.com.ifpr.edu.sdpe_backend.domain.enums.TipoPerfil;
import br.com.ifpr.edu.sdpe_backend.exception.EntityNotFoundException;
import br.com.ifpr.edu.sdpe_backend.repository.ContaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthorizationService implements UserDetailsService {

    private final ContaRepository contaRepository;
    private final ParticipanteService participanteService;
    private final CoordenadorService coordenadorService;
    private final EmailService emailService;
    private final ContaRepository repository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return contaRepository.findByEmail(email);
    }

    @Transactional
    public void registrarUsuario(RegisterDTO data) {
        if (this.contaRepository.findByEmail(data.email()) != null)
            throw new IllegalArgumentException("E-mail já cadastrado.");

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.senha());

        Conta novaConta = Conta.builder()
                .email(data.email())
                .senha(encryptedPassword)
                .perfil(data.perfil())
                .ativo(true)
                .build();

        this.contaRepository.save(novaConta);

        if (data.perfil() == TipoPerfil.COORDENADOR) {
            Coordenador coordenador = Coordenador.builder()
                    .nome(data.nome())
                    .dataNascimento(data.dataNascimento())
                    .vinculoInstitucional(data.vinculoInstitucional())
                    .cpf(data.cpf())
                    .cidade(data.cidade())
                    .contato(data.email())
                    .conta(novaConta)
                    .build();

            this.coordenadorService.salvar(coordenador);

        } else if (data.perfil() == TipoPerfil.PARTICIPANTE) {
            Participante participante = Participante.builder()
                    .nome(data.nome())
                    .dataNascimento(data.dataNascimento())
                    .cpf(data.cpf())
                    .cidade(data.cidade())
                    .vinculoInstitucional(data.vinculoInstitucional())
                    .conta(novaConta)
                    .build();

            this.participanteService.salvar(participante);
        }
    }

    public void solicitarRecuperacao(String email) {
        // Cast necessário se o repositório retornar UserDetails
        Conta conta = (Conta) repository.findByEmail(email);

        if (conta == null) throw new EntityNotFoundException(email);

        // Regra de Negócio: Gerar código e validade
        String codigo = String.format("%06d", new Random().nextInt(999999));
        conta.setCodigoRecuperacao(codigo);
        conta.setDataExpiracaoCodigo(Instant.now().plus(1, ChronoUnit.MINUTES));

        repository.save(conta);

        emailService.enviarEmailRecuperacao(conta.getEmail(), codigo);
    }

    public void redefinirSenha(String email, String codigo, String novaSenha) {
        Conta conta = (Conta) repository.findByEmail(email);

        if (conta == null) throw new EntityNotFoundException("Conta não encontrada.");

        if (conta.getCodigoRecuperacao() == null ||
                !conta.getCodigoRecuperacao().equals(codigo) ||
                conta.getDataExpiracaoCodigo().isBefore(Instant.now())) {
            throw new IllegalArgumentException("Código inválido ou expirado.");
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(novaSenha);
        conta.setSenha(encryptedPassword);

        conta.setCodigoRecuperacao(null);
        conta.setDataExpiracaoCodigo(null);

        repository.save(conta);
    }
}