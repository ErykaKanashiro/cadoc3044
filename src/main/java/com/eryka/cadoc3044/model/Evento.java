package com.eryka.cadoc3044.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class Evento {

    private int acao;
    private String ipoc;
    private double saldoDevedor;
    private String atraso;
    private List<Pagamento> pagamentos;
    private List<Concessao> concessoes;
}
