package com.eryka.cadoc3044.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class EventoBaseDTO {

    @Min(value = 1)
    @Max(value = 2)
    protected int acao;

    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Data deve estar no formato yyyy-MM-dd")
    protected String data;

    @Pattern(regexp = "\\d{9}", message = "class3050 deve conter 9 dígitos")
    protected String class3050;

    @DecimalMin(value = "0.00", message = "Valor deve ser maior ou igual a 0")
    @Digits(integer = 11, fraction = 2, message = "Valor deve ter até 11 dígitos inteiros e 2 casas decimais")
    protected double valor;

}
