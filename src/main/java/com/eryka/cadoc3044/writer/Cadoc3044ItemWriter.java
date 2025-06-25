package com.eryka.cadoc3044.writer;

import com.eryka.cadoc3044.dto.EventoOperacaoContexto;
import com.eryka.cadoc3044.dto.EventoOperacaoDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

@Slf4j
public class Cadoc3044ItemWriter implements ItemWriter<EventoOperacaoContexto> {

    @Override
    public void write(Chunk<? extends EventoOperacaoContexto> chunk) throws Exception {
        List<? extends EventoOperacaoContexto> items = chunk.getItems();
        for (EventoOperacaoContexto item : items) {
            log.info("Escrevendo operação: {} com CNPJ: {}", item.getOperacao(), item.getCnpjIF());
        }
    }
}
