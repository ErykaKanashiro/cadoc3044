package com.eryka.cadoc3044.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TbEventoRejeitado")
public class EventoRejeitadoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CdEventoRejeitado")
    private Integer id;

    @Column(name = "DtHora", nullable = false)
    private LocalDateTime hora;

    @Column(name = "NmArquivo")
    private String nomeArquivo;

    @Column(name = "NrLinhaArquivo")
    private Integer linhaArquivo;

    @ManyToOne
    @JoinColumn(name = "CdSistemaOrigem")
    private SistemaOrigemEntity sistemaOrigem;

    @Column(name = "CdAcao", nullable = false)
    private String acao;

    @Column(name = "JsDadosRecebidos", nullable = false, columnDefinition = "jsonb")
    private String dadosRecebidos;

    @ManyToOne
    @JoinColumn(name = "CdMotivoRejeicao")
    private MotivoRejeicaoEntity motivoRejeicao;

    @ManyToOne
    @JoinColumn(name = "NmCampoErro")
    private CampoErroEntity campoErro;

    @Column(name = "FlAtivo", nullable = false)
    private String ativo;

    @Column(name = "DtInclusao", nullable = false)
    private LocalDateTime inclusao;

    @Column(name = "DtAlteracao", nullable = false)
    private LocalDateTime alteracao;

}