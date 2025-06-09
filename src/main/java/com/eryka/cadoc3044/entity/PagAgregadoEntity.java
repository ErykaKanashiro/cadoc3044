package com.eryka.cadoc3044.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "TbPagAgregados")
public class PagAgregadoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CdPagAgregado")
    private Integer id;

    @Column(name = "DtReferencia", nullable = false)
    private LocalDate referencia;

    @Column(name = "CdClass3050", nullable = false)
    private String class3050;

    @Column(name = "VlAgregado", nullable = false)
    private BigDecimal valorAgregado;

    @Column(name = "CdAcao", nullable = false)
    private String acao;

    @ManyToOne
    @JoinColumn(name = "CdArquivoRemessaOrigem")
    private ArquivoRemessaOrigemEntity arquivoRemessaOrigem;

    @ManyToOne
    @JoinColumn(name = "CdArquivoRemessaEnvio")
    private ArquivoRemessaEnvioEntity arquivoRemessaEnvio;

    @Column(name = "FlAtivo", nullable = false)
    private String ativo;

    @Column(name = "DtInclusao", nullable = false)
    private LocalDateTime inclusao;

    @Column(name = "DtAlteracao", nullable = false)
    private LocalDateTime alteracao;

}