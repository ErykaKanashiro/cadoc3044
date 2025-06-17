import com.eryka.cadoc3044.dto.EventoOperacaoDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class Cadoc3044Processor implements ItemProcessor<EventoOperacaoDTO, EventoOperacaoDTO> {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final PubSubService pubSubService;

    public Cadoc3044Processor(PubSubService pubSubService) {
        this.pubSubService = pubSubService;
    }

    @Override
    public EventoOperacaoDTO process(EventoOperacaoDTO item) {
        Set<ConstraintViolation<EventoOperacaoDTO>> violations = validator.validate(item);

        if (!violations.isEmpty()) {
            String erro = violations.stream()
                    .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                    .collect(Collectors.joining("; "));

            try {
                String json = objectMapper.writeValueAsString(item);
                pubSubService.publicarRejeicao(json, erro);
            } catch (JsonProcessingException e) {
                log.error("Erro ao serializar item rejeitado: {}", e.getMessage());
            }

            log.error("Erro de validação no item: {}", erro);
            return null;
        }

        return item;
    }
}