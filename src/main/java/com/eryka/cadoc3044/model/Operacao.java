package com.eryka.cadoc3044.model;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Operacao {
    private int acao;
    private String ipoc;
    private double saldoDevedor;
    private String atraso;
    private List<Pagamento> pagamentos;
    private List<Concessao> concessoes;
}
