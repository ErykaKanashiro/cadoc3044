package com.eryka.cadoc3044.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Evento {

    private String cnpjIF;
    private String dataHoraRemessa;
    private String database;
    private List<Operacao> operacoes;
    private List<PagAgregado> pagAgregados;

}
