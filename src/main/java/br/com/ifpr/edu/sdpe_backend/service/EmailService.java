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
}