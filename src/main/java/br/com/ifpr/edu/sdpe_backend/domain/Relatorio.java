package br.com.ifpr.edu.sdpe_backend.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.JOINED) // porque é abstrata
@Table(name = "tb_relatorio")
public abstract class Relatorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String descricao;

    private Date semestre;

    @ManyToOne
    @JoinColumn(name = "projeto_id")
    private Projeto projeto;

    public Relatorio() {
        nome =  " ";
        descricao = " ";
        semestre = new Date();
    }

   // abstract void getRelatorioTipo();

}