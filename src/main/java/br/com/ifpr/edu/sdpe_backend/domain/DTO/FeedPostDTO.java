package br.com.ifpr.edu.sdpe_backend.domain.DTO;

import java.util.Date;

public record FeedPostDTO(
        Long id,
        String titulo,
        String descricao,
        String imagemCapa,
        String autores,
        Date dataPublicacao
) {}
