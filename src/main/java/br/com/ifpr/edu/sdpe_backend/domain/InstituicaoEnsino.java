package br.com.ifpr.edu.sdpe_backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @Column(nullable = false, unique = false)
    private String nome;

    private String cidade;

    private String estado;

    @OneToMany(mappedBy = "instituicaoEnsino")
    @JsonIgnore
    private List<Projeto> projetos;

    public InstituicaoEnsino(){
        nome = " ";
        cidade = " ";
        projetos = new ArrayList<>();
    }

}