package br.com.ifpr.edu.sdpe_backend.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Aluno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String contato;

    private String telefone;

    private String email;

    @ManyToMany
    private List<Projeto> projeto;
}
