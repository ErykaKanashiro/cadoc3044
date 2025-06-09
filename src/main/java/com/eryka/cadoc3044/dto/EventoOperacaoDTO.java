package com.eryka.cadoc3044.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class EventoOperacaoDTO {

    @NotNull
    @Min(value = 1)
    @Max(value = 2)
    private int acao;
    @NotNull
    private String ipoc;
    @NotNull
    @DecimalMin("0.00")
    private double saldoDevedor;
    @NotNull
    @Pattern(regexp = "[SN]", message = "Atraso deve ser S ou N")
    private String atraso;
    @Valid
    private List<PagamentoDTO> pagamentos;
    @Valid
    private List<ConcessaoDTO> concessoes;

}
