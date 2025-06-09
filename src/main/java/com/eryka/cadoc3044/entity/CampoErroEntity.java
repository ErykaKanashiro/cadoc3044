package com.eryka.cadoc3044.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TbCampoErro")
public class CampoErroEntity {
    @Id
    @Column(name = "NmCampo")
    private String nomeCampo;

    @Column(name = "DsCampo")
    private String descricaoCampo;

    @Column(name = "FlAtivo", nullable = false)
    private String ativo;

    @Column(name = "DtInclusao", nullable = false)
    private LocalDateTime inclusao;

    @Column(name = "DtAlteracao", nullable = false)
    private LocalDateTime alteracao;

}