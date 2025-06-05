package com.eryka.cadoc3044.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Cadoc3044 {
    @Pattern(regexp = "\\d{8}")
    @NotNull
    private String cnpjIF;
    @NotNull
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}")
    private String dataHoraRemessa;
    @NotNull
    @Pattern(regexp = "\\d{4}-\\d{2}")
    private String database;
    @Valid
    private List<EventoOperacao> operacoes;
    @Valid
    private List<EventoPagAgregado> pagAgregados;
}