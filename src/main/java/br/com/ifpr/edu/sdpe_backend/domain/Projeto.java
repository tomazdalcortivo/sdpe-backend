package br.com.ifpr.edu.sdpe_backend.domain;

import br.com.ifpr.edu.sdpe_backend.domain.enums.TipoFormato;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Entity
public class Projeto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String imagem;

    private String descricao;

    private String cargaHoraria;

    private TipoFormato formato;

    @OneToOne
    private Coordenador coordenador;

    @ManyToMany
    private List<Participante> participantes;
}
