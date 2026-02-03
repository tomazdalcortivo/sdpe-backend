package br.com.ifpr.edu.sdpe_backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_documento")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Documento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String url;

    @ManyToOne
    @JoinColumn(name = "projeto_id")
    @JsonIgnore
    private Projeto projeto;
}