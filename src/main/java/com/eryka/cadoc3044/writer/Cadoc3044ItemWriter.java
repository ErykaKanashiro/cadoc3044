package com.eryka.cadoc3044.writer;

import com.eryka.cadoc3044.model.EventoOperacao;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class Cadoc3044ItemWriter implements ItemWriter<EventoOperacao> {


    @Override
    public void write(Chunk<? extends EventoOperacao> chunk) throws Exception {
        List<? extends EventoOperacao> items = chunk.getItems();
        for (EventoOperacao eventoOperacao : items) {
            items.forEach(System.out::println);
        }
    }
}
