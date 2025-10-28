package br.com.ifpr.edu.sdpe_backend.domain;

import br.com.ifpr.edu.sdpe_backend.domain.enums.TipoContato;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@Table(name = "tb_contato")
public class Contato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mensagem;

    private TipoContato tipoContato;

    @CreationTimestamp
    private Instant dataEnvio;

    @ManyToOne
    @JoinColumn(name = "projeto_id")
    private Projeto projeto;

    public Contato() {
        mensagem = " ";
        tipoContato = TipoContato.CHAMADO;
        dataEnvio = Instant.now();
        projeto = new Projeto();
    }

}