package br.com.ifpr.edu.sdpe_backend.domain;

import br.com.ifpr.edu.sdpe_backend.domain.enums.FuncaoCoordenador;
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

    private String cargoInstituicao;

    private String contato;

    @ManyToMany(mappedBy = "coordenadores")
    @JsonIgnore
    private List<Projeto> projetos;

    @Enumerated(EnumType.STRING)
    private FuncaoCoordenador funcao;

    public Coordenador(){
        cargoInstituicao = " ";
        contato = " ";
        projetos = new ArrayList<>();
        funcao = FuncaoCoordenador.COORDENADOR_GERAL;
    }

}