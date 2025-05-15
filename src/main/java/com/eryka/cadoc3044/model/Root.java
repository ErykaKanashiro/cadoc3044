package com.eryka.cadoc3044.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Root {
    private String cnpjIF;
    private String dataHoraRemessa;
    private String database;
    private List<Evento> operacoes;
    private List<PagAgregado> pagAgregados;
}