package br.com.ifpr.edu.sdpe_backend.domain;

import br.com.ifpr.edu.sdpe_backend.domain.enums.TipoFormato;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "tb_projeto")
@Builder
@AllArgsConstructor
public class Projeto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String descricao;

    private String area;

    private Boolean status;

    @PastOrPresent(message = "A data de início não pode ser futura")
    private Date dataInicio;

    @FutureOrPresent(message = "A data de fim não pode ser anterior à data atual")
    private Date dataFim;

    private Double cargaHoraria;

    private String imagemPath;

    @ManyToOne
    @JoinColumn(name = "instituicao_id")
    private InstituicaoEnsino instituicaoEnsino;

    private TipoFormato formato;

    @OneToMany(mappedBy = "projeto")
    private List<Coordenador> coordenadores;

    @OneToMany(mappedBy = "projeto")
    private List<Relatorio> relatorios;

    @ManyToMany
    @JoinTable(
            name = "projeto_participante",
            joinColumns = @JoinColumn(name = "projeto_id"),
            inverseJoinColumns = @JoinColumn(name = "participante_id")
    )
    @JsonIgnore
    private List<Participante> participantes;

    @OneToMany(mappedBy = "projeto")
    private List<Contato> contatos;

    public Projeto() {
        nome = " ";
        descricao = "";
        area = " ";
        status = false;
        dataInicio = new Date();
        dataFim = new Date();
        cargaHoraria = 0.0;
        instituicaoEnsino = new InstituicaoEnsino();
        formato = TipoFormato.PRESENCIAL;
        coordenadores = new ArrayList();
        participantes = new ArrayList<>();
        contatos = new ArrayList<>();
        imagemPath = " ";

    }
}