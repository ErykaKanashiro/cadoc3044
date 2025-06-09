package com.eryka.cadoc3044.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TbRelatorioEnvioOrigens")
public class RelatorioEnvioOrigensEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CdRelatorioEnvio")
    private Integer id;

    @Column(name = "DtGeracao", nullable = false)
    private LocalDateTime geracao;

    @Column(name = "DsCaminhoArquivo", nullable = false)
    private String caminhoArquivo;

    @Column(name = "DsTipoRelatorio", nullable = false)
    private String tipoRelatorio;

    @Column(name = "FlAtivo", nullable = false)
    private String ativo;

    @Column(name = "DtInclusao", nullable = false)
    private LocalDateTime inclusao;

    @Column(name = "DtAlteracao", nullable = false)
    private LocalDateTime alteracao;

}