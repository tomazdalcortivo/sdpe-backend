package br.com.ifpr.edu.sdpe_backend.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.br.CPF;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@Entity
@Table(name = "tb_participante")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Participante {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "participante_seq")
    @SequenceGenerator(name = "participante_seq", sequenceName = "participante_seq", allocationSize = 1)
    private Long id;

    @Pattern(
            regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ]+( [A-Za-zÀ-ÖØ-öø-ÿ]+)+$",
            message = "Digite o nome completo (nome e sobrenome)"
    )
    @NotBlank(message = "O nome não pode ser vazio")
    private String nome;

    @PastOrPresent(message = "A data não pode ser maior que hoje")
    private Date dataNascimento;

    @CPF(message = "Informe um cpf valido")
    private String cpf;

    private String cidade;

    @Column(columnDefinition = "TEXT")
    private String resumo;

    private String telefone;

    private Boolean vinculoInstitucional;

    @ManyToMany(mappedBy = "participantes")
    private List<Projeto> projetos;

    @OneToOne
    @JoinColumn(name = "conta_id")
    private Conta conta;

    @OneToMany(mappedBy = "participante")
    private List<Notificacao> notificacoes;

    public Participante() {
        vinculoInstitucional = true;
        projetos = new ArrayList();
    }

}