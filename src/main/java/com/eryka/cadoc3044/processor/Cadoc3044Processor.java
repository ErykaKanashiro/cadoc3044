package com.eryka.cadoc3044.processor;

import com.eryka.cadoc3044.dto.EventoOperacaoContexto;
import com.eryka.cadoc3044.dto.EventoOperacaoDTO;
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
public class Cadoc3044Processor implements ItemProcessor<EventoOperacaoContexto, EventoOperacaoContexto> {

    private final Validator validator;

    public Cadoc3044Processor() {
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Override
    public EventoOperacaoContexto process(EventoOperacaoContexto item) throws Exception {
        log.info("Processing item: {}", item);

        Set<ConstraintViolation<EventoOperacaoDTO>> violations = validator.validate(item.getOperacao());
        if (!violations.isEmpty()) {
            String errorMessage = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", "));
            log.info("Validation errors: {}", errorMessage);
            return null; // Skip this item if validation fails
        }
        return item;
    }
}