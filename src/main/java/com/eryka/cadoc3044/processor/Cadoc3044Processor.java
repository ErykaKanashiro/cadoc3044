package com.eryka.cadoc3044.processor;

import com.eryka.cadoc3044.model.EventoOperacao;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class Cadoc3044Processor implements ItemProcessor<EventoOperacao, EventoOperacao> {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Override
    public EventoOperacao process(EventoOperacao item) {
        Set<ConstraintViolation<EventoOperacao>> violations = validator.validate(item);

        if (!violations.isEmpty()) {
            String erro = violations.stream()
                    .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                    .collect(Collectors.joining("; "));

            // logar, publicar no Pub/Sub ou gravar em tabela de rejeição
            System.err.println("Evento inválido: " + erro);

            return null; // evento rejeitado → não será escrito no banco de dados
        }

        return item; // válido → segue para o writer
    }
}