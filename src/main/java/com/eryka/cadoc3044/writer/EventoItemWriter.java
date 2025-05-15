package com.eryka.cadoc3044.writer;

import com.eryka.cadoc3044.model.Evento;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class EventoItemWriter implements ItemWriter<Evento> {


    @Override
    public void write(Chunk<? extends Evento> chunk) throws Exception {
        List<? extends Evento> items = chunk.getItems();
        for (Evento evento : items) {
            items.forEach(System.out::println);
        }
    }
}
