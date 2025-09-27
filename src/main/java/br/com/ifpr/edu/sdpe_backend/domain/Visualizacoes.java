package br.com.ifpr.edu.sdpe_backend.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Visualizacoes extends Relatorio{

    private Integer quantVisualizacoes;

    @Override
    public void getRelatorioTipo() {}

}