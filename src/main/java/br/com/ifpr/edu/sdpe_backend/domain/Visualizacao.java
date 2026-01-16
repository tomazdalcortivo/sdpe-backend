package br.com.ifpr.edu.sdpe_backend.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Entity
@Table(name = "tb_visualizacao")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Visualizacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "projeto_id")
    private Projeto projeto;

    @CreationTimestamp
    private LocalDate dataVisualizacao;

}