package com.eryka.cadoc3044.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "TbArquivoRemessaEnvio")
public class ArquivoRemessaEnvioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CdArquivoRemessaEnvio")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "CdSistemaOrigem")
    private SistemaOrigemEntity sistemaOrigem;

    @Column(name = "NmArquivo", nullable = false)
    private String nomeArquivo;

    @Column(name = "DtBase", nullable = false)
    private LocalDate base;

    @Column(name = "DtHoraRemessa", nullable = false)
    private LocalDateTime horaRemessa;

    @Column(name = "DtProximaRemessa", nullable = false)
    private LocalDateTime proximaRemessa;

    @Column(name = "FlEnviado", nullable = false)
    private String enviado;

    @Column(name = "FlAtivo", nullable = false)
    private String ativo;

    @Column(name = "DtInclusao", nullable = false)
    private LocalDateTime inclusao;

    @Column(name = "DtAlteracao", nullable = false)
    private LocalDateTime alteracao;

}