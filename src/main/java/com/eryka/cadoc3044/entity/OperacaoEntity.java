package com.eryka.cadoc3044.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "TbOperacao")
public class OperacaoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CdOperacao")
    private Integer id;

    @Column(name = "CdEventoOperacao", nullable = false)
    private String eventoOperacao;

    @Column(name = "Ipoc", nullable = false)
    private String ipoc;

    @Column(name = "CdAcao", nullable = false)
    private String acao;

    @Column(name = "DtEvento", nullable = false)
    private LocalDate evento;

    @Column(name = "VrSaldoDevedor", nullable = false)
    private BigDecimal saldoDevedor;

    @Column(name = "FlAtraso", nullable = false)
    private String atraso;

    @ManyToOne
    @JoinColumn(name = "CdArquivoRemessaOrigem")
    private ArquivoRemessaOrigemEntity arquivoRemessaOrigem;

    @ManyToOne
    @JoinColumn(name = "CdArquivoRemessaEnvio")
    private ArquivoRemessaEnvioEntity arquivoRemessaEnvio;

    @Column(name = "FlConsolidado", nullable = false)
    private LocalDateTime consolidado;

    @Column(name = "DtHoraConsolidado", nullable = false)
    private LocalDateTime horaConsolidado;

    @Column(name = "FlAtivo", nullable = false)
    private String ativo;

    @Column(name = "DtInclusao", nullable = false)
    private LocalDateTime inclusao;

    @Column(name = "DtAlteracao", nullable = false)
    private LocalDateTime alteracao;

}