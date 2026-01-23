package br.com.ifpr.edu.sdpe_backend.domain;

import br.com.ifpr.edu.sdpe_backend.domain.enums.TipoFormato;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

    private Date dataInicio;

    private Date dataFim;

    private Double cargaHoraria;

    private String imagemPath;

    private String documentoPath;

    @ManyToOne
    @JoinColumn(name = "instituicao_id")
    private InstituicaoEnsino instituicaoEnsino;

    @Enumerated(EnumType.STRING)
    private TipoFormato formato;

    @ManyToMany
    @JoinTable(
            name = "projeto_coordenador",
            joinColumns = @JoinColumn(name = "projeto_id"),
            inverseJoinColumns = @JoinColumn(name = "coordenador_id")
    )
    private List<Coordenador> coordenadores;
    @ElementCollection
    @CollectionTable(
            name = "projeto_redes_sociais",
            joinColumns = @JoinColumn(name = "projeto_id"))
    @MapKeyColumn(name = "rede_social")
    @Column(name = "url")
    private Map<String, String> redesSociais;

    @ManyToMany
    @JoinTable(
            name = "projeto_participante",
            joinColumns = @JoinColumn(name = "projeto_id"),
            inverseJoinColumns = @JoinColumn(name = "participante_id")
    )
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
        documentoPath = "";
    }
}