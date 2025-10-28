package br.com.ifpr.edu.sdpe_backend.domain;

import br.com.ifpr.edu.sdpe_backend.domain.enums.FuncaoCoordenador;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@Entity
@Table(name = "tb_coordenador")
public class Coordenador extends Participante {

    private String cargoInstituicao;

    private String contato;

    @ManyToOne
    @JoinColumn(name = "projeto_id")
    private Projeto projeto;

    @Enumerated(EnumType.STRING)
    private FuncaoCoordenador funcao;

    public Coordenador(){
        cargoInstituicao = " ";
        contato = " ";
        projeto = null;
        funcao = FuncaoCoordenador.COORDENADOR_GERAL;
    }

}