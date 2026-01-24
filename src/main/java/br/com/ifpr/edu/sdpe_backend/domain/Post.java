package br.com.ifpr.edu.sdpe_backend.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "tb_post")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String conteudo;

    private String mediaUrl;

    private Date dataPublicacao;

    @ManyToOne
    @JoinColumn(name = "autor_id")
    private Coordenador autor;

    @ManyToOne
    @JoinColumn(name = "projeto_id")
    @JsonIgnore
    private Projeto projeto;
}
