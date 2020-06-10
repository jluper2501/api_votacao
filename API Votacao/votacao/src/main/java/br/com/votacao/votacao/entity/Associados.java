package br.com.votacao.votacao.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@Entity
@Table(name = "associados")
@AllArgsConstructor
@NoArgsConstructor
public class Associados {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "cpf_associado")
    private String cpfAssociado;

    @Column(name = "id_pauta")
    private Integer idPauta;
}
