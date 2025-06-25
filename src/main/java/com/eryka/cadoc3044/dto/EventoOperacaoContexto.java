package com.eryka.cadoc3044.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EventoOperacaoContexto {
    private EventoOperacaoDTO operacao;
    private String cnpjIF;
    private String dataHoraRemessa;
    private String database;
}