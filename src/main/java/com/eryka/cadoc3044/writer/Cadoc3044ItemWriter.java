package com.eryka.cadoc3044.writer;

import com.eryka.cadoc3044.dto.EventoOperacaoDTO;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class Cadoc3044ItemWriter implements ItemWriter<EventoOperacaoDTO> {


    @Override
    public void write(Chunk<? extends EventoOperacaoDTO> chunk) throws Exception {
        List<? extends EventoOperacaoDTO> items = chunk.getItems();
        for (EventoOperacaoDTO eventoOperacaoDTO : items) {
            items.forEach(System.out::println);
        }
    }
}
