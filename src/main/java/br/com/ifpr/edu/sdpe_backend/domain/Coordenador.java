package br.com.ifpr.edu.sdpe_backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@Entity
@Table(name = "tb_coordenador")
public class Coordenador extends Participante {

    private String telefone;

    @ManyToMany(mappedBy = "coordenadores")
    @JsonIgnore
    private List<Projeto> meusProjetosCriados;

    public Coordenador() {
        this.meusProjetosCriados = new ArrayList<>();
    }

}