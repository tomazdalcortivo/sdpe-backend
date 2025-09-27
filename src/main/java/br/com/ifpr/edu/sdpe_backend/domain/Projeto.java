package br.com.ifpr.edu.sdpe_backend.domain;

import br.com.ifpr.edu.sdpe_backend.domain.enums.TipoFormato;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
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

    public Projeto() {
        area = " ";
        cargaHoraria = 0.0;
        coordenador = new Coordenador();
        dataFim = new Date();
        dataInicio = new Date();
        descricao = "";
        feedbacks = new ArrayList<>();
        formato = TipoFormato.PRESENCIAL;
        instituicaoVinculada = new InstituicaoEnsino();
        nome = " ";
        participantes = new ArrayList<>();
        planejamento = " ";
        status = false;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String descricao;

    private String area;

    private Boolean status;

    private Date dataInicio;

    private Date dataFim;

    private Double cargaHoraria;

    @Transient
    private InstituicaoEnsino instituicaoVinculada;

    private String planejamento;

    private TipoFormato formato;

    @OneToOne
    private Coordenador coordenador;

    @ManyToMany
    private List<Participante> participantes;

    @Transient
    private List<Contato> feedbacks;

}