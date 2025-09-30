package br.com.ifpr.edu.sdpe_backend.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@Entity
@Builder
@Table(name = "tb_participante")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Participante {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "participante_seq")
    @SequenceGenerator(name = "participante_seq", sequenceName = "participante_seq", allocationSize = 1)
    private Long id;

    private String nome;

    private Date dataNascimento;

    //@CPF (validar depois!!)
    private String cpf;

    private String cidade;

    private Boolean vinculoInstitucional;

    @ManyToMany(mappedBy = "participantes")
    private List<Projeto> projetos;

    @OneToOne
    @JoinColumn(name = "conta_id")
    private Conta conta;

    @OneToMany(mappedBy = "participante")
    private List<Notificacao> notificacoes;

    public Participante() {
        nome = " ";
        dataNascimento = new Date();
        cpf = " ";
        cidade = " ";
        vinculoInstitucional = true;
        projetos = new ArrayList();
        //conta = new Conta();
    }

}