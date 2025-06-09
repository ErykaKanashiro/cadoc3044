package com.eryka.cadoc3044.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "TbSistemaOrigem")
public class SistemaOrigemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CdSistemaOrigem")
    private Integer id;

    @Column(name = "SgSistemaOrigem", nullable = false)
    private String sigla;

    @Column(name = "NmOrigem", nullable = false)
    private String nome;

    @Column(name = "FlObrigatoria", nullable = false)
    private String obrigatoria;

    @Column(name = "DsEmailResponsavel", nullable = false)
    private String emailResponsavel;

    @Column(name = "DsEmailGrupo", nullable = false)
    private String emailGrupo;

    @Column(name = "FlAtivo", nullable = false)
    private String ativo;

    @Column(name = "DtInclusao", nullable = false)
    private LocalDateTime inclusao;

    @Column(name = "DtAlteracao", nullable = false)
    private LocalDateTime alteracao;
}