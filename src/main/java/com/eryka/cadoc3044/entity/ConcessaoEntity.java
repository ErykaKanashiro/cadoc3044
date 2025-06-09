package com.eryka.cadoc3044.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "TbConcessao")
public class ConcessaoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CdConcessao")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "CdOperacao")
    private OperacaoEntity operacao;

    @Column(name = "CdEventoConcessao", nullable = false)
    private String eventoConcessao;

    @Column(name = "CdAcao", nullable = false)
    private String acao;

    @Column(name = "DtConcessao", nullable = false)
    private LocalDate concessao;

    @Column(name = "CdClass3050", nullable = false)
    private String class3050;

    @Column(name = "VrConcessao", nullable = false)
    private BigDecimal valor;

    @Column(name = "FlAtivo", nullable = false)
    private String ativo;

    @Column(name = "DtInclusao", nullable = false)
    private LocalDateTime inclusao;

    @Column(name = "DtAlteracao", nullable = false)
    private LocalDateTime alteracao;

}