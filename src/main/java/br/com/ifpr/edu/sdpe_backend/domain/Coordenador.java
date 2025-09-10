package br.com.ifpr.edu.sdpe_backend.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Coordenador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Conta conta;

    private String nome;

    private String contato;

    private String telefone;

    private String email;

    @ManyToMany
    private List<Projeto> projeto;
}
