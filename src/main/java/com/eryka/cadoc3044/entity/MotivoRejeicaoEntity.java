package com.eryka.cadoc3044.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TbMotivoRejeicao")
public class MotivoRejeicaoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CdMotivoRejeicao")
    private Integer id;

    @Column(name = "CdMotivo", nullable = false, unique = true)
    private String codigo;

    @Column(name = "DsMotivo", nullable = false)
    private String descricao;

    @Column(name = "FlAtivo", nullable = false)
    private String ativo;

    @Column(name = "DtInclusao", nullable = false)
    private LocalDateTime inclusao;

    @Column(name = "DtAlteracao", nullable = false)
    private LocalDateTime alteracao;

    // getters e setters
}