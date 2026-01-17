package br.com.ifpr.edu.sdpe_backend.repository;

import br.com.ifpr.edu.sdpe_backend.domain.InstituicaoEnsino;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InstituicaoEnsinoRepository extends JpaRepository<InstituicaoEnsino, Long> {

    Optional<InstituicaoEnsino> findByNome(String nome);

    Optional<InstituicaoEnsino> findByCidade(String cidade);

    Optional<InstituicaoEnsino> findByNomeAndCidade(String nome, String cidade);

}