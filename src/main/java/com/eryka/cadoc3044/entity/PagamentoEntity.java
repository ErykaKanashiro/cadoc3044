package com.eryka.cadoc3044.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "TbPagamento")
public class PagamentoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CdPagamento")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "CdOperacao")
    private OperacaoEntity operacao;

    @Column(name = "CdEventoPagamento", nullable = false)
    private String eventoPagamento;

    @Column(name = "CdAcao", nullable = false)
    private String acao;

    @Column(name = "DtPagamento", nullable = false)
    private LocalDate pagamento;

    @Column(name = "CdClass3050", nullable = false)
    private String class3050;

    @Column(name = "VrPagamento", nullable = false)
    private BigDecimal valor;

    @Column(name = "FlAtivo", nullable = false)
    private String ativo;

    @Column(name = "DtInclusao", nullable = false)
    private LocalDateTime inclusao;

    @Column(name = "DtAlteracao", nullable = false)
    private LocalDateTime alteracao;

}