package com.eryka.cadoc3044.processor;

import com.eryka.cadoc3044.model.Evento;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EventoProcessor implements ItemProcessor<Evento, Evento> {

    private final KafkaTemplate<String, Evento> kafkaTemplate;
    private static final String TOPIC_NAME = "cadoc3044-topico";

    public EventoProcessor(KafkaTemplate<String, Evento> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public Evento process(Evento evento) {
        log.info("Processando evento: {}", evento);

        kafkaTemplate.send(TOPIC_NAME, evento).exceptionally(ex -> {
            log.error("Erro ao enviar evento: {}", evento, ex);
            return null;
        });

        return evento;
    }
}