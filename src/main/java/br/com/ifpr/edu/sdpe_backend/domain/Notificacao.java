package br.com.ifpr.edu.sdpe_backend.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Builder
@Table(name = "tb_notificacao")
public class Notificacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "participante_id")
    private Participante participante;

    private String mensagem;

    @CreationTimestamp
    private Instant dataEnvio;

    public Notificacao(){
        participante = new Participante();
        mensagem = "";
        dataEnvio = Instant.now();
    }

}