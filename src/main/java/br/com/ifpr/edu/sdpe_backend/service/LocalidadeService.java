package br.com.ifpr.edu.sdpe_backend.service;

import br.com.ifpr.edu.sdpe_backend.domain.DTO.CidadeDTO;
import br.com.ifpr.edu.sdpe_backend.domain.DTO.EstadoDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class LocalidadeService {
    private static final String IBGE_BASE =
            "https://servicodados.ibge.gov.br/api/v1/localidades";

    private final RestTemplate restTemplate = new RestTemplate();

    public List<EstadoDTO> buscarEstados() {
        EstadoDTO[] estados = restTemplate.getForObject(
                IBGE_BASE + "/estados?orderBy=nome",
                EstadoDTO[].class
        );
        return Arrays.asList(estados);
    }

    public List<CidadeDTO> buscarCidadesPorUf(String uf) {
        CidadeDTO[] cidades = restTemplate.getForObject(
                IBGE_BASE + "/estados/{uf}/municipios?orderBy=nome",
                CidadeDTO[].class,
                uf
        );
        return Arrays.asList(cidades);
    }
}
