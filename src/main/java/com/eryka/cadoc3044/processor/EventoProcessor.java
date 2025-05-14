package com.eryka.cadoc3044.processor;

import com.eryka.cadoc3044.model.Evento;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class EventoProcessor implements ItemProcessor<Evento, Evento> {

    private final KafkaTemplate<String, Evento> kafkaTemplate;

    private final String topicName = "cadoc3044-topic";

    public EventoProcessor(KafkaTemplate<String, Evento> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public Evento process(Evento evento) throws Exception {
        kafkaTemplate.send(topicName, evento);
        return evento;
    }
}