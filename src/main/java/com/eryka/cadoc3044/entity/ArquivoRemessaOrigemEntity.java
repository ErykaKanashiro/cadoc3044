package com.eryka.cadoc3044.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TbArquivoRemessaOrigem")
public class ArquivoRemessaOrigemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CdArquivoRemessaOrigem")
    private Integer id;

    @Column(name = "CdCnpjIF", nullable = false)
    private String cnpjIf;

    @Column(name = "DtHoraRemessa", nullable = false)
    private LocalDateTime horaRemessa;

    @Column(name = "NmArquivo", nullable = false)
    private String nomeArquivo;

    @ManyToOne
    @JoinColumn(name = "CdSistemaOrigem")
    private SistemaOrigemEntity sistemaOrigem;

    @Column(name = "DtReferencia", nullable = false)
    private LocalDateTime referencia;

    @Column(name = "DtBase", nullable = false)
    private LocalDateTime base;

    @Column(name = "FlAtivo", nullable = false)
    private String ativo;

    @Column(name = "DtInclusao", nullable = false)
    private LocalDateTime inclusao;

    @Column(name = "DtAlteracao", nullable = false)
    private LocalDateTime alteracao;

    // getters e setters
}