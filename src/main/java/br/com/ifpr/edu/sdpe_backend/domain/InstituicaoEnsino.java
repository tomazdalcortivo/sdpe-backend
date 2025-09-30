package br.com.ifpr.edu.sdpe_backend.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "tb_instituicaoEnsino")
@AllArgsConstructor
@Builder
public class InstituicaoEnsino {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String cidade;

    private String descricao;

    @OneToMany(mappedBy = "instituicaoEnsino")
    private List<Projeto> projetos;

    public InstituicaoEnsino(){
        nome = " ";
        cidade = " ";
        descricao = " ";
        projetos = new ArrayList<>();
    }

}