package br.com.ifpr.edu.sdpe_backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String remetente;

    public void enviarEmailRecuperacao(String destinatario, String codigo) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(remetente != null ? remetente : "noreply@sdpe.com.br");

        message.setTo(destinatario);
        message.setSubject("SDPE - Recuperação de Senha");
        message.setText("Seu código de recuperação é: " + codigo + "\n\nEste código expira em 15 minutos.");

        mailSender.send(message);
    }

    public void enviarRespostaSuporte(String destinatario, String nomeContato, String mensagemResposta) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(destinatario);
        message.setSubject("Resposta ao seu contato - SDPE");
        message.setText(
                "Olá " + nomeContato + ",\n\n" +
                        "Recebemos o seu contato através da nossa plataforma.\n\n" +
                        "Resposta da Administração:\n" +
                        mensagemResposta + "\n\n" +
                        "Atenciosamente,\n" +
                        "Equipe SDPE"
        );

        try {
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao enviar email.");
        }
    }

    public void enviarEmailRejeicaoCadastro(String destinatario, String nome, String motivo) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(destinatario);
        message.setSubject("SDPE - Atualização sobre seu cadastro");

        message.setText(
                String.format("""
                        Olá, %s.
                        
                        Informamos que sua solicitação de cadastro no sistema SDPE foi analisada.
                        Infelizmente, o cadastro não foi aprovado neste momento.
                        
                        Motivo informado pela administração:
                        "%s"
                        
                        Caso julgue necessário, você pode realizar um novo cadastro corrigindo as informações apontadas ou entrar em contato conosco.
                        
                        Atenciosamente,
                        Equipe SDPE
                        """, nome, motivo)
        );

        mailSender.send(message);
    }
}