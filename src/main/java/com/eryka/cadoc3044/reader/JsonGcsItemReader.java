// src/main/java/com/eryka/cadoc3044/reader/JsonGcsItemReader.java
package com.eryka.cadoc3044.reader;

import com.eryka.cadoc3044.dto.Cadoc3044DTO;
import com.eryka.cadoc3044.dto.EventoOperacaoContexto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;
import java.nio.channels.Channels;
import java.util.Iterator;
import java.util.List;

@Slf4j
@Configuration
public class JsonGcsItemReader {

    private final ObjectMapper objectMapper;

    public JsonGcsItemReader(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    private boolean isNomeArquivoValido(String nomeArquivo) {
        return nomeArquivo != null && nomeArquivo.matches("cadoc3044_exemplo.json");
    }

    @Bean
    @StepScope
    public ItemReader<EventoOperacaoContexto> eventoOperacaoContextoItemReader(
            @Value("#{stepExecutionContext['fileName']}") String gcsFilePath,
            @Value("#{stepExecutionContext['bucketName']}") String bucketName,
            Storage storage) {
        return new ItemReader<>() {
            private Iterator<EventoOperacaoContexto> iterator;

            @Override
            public EventoOperacaoContexto read() throws Exception {
                if (!isNomeArquivoValido(gcsFilePath)) {
                    log.warn("Ignorando arquivo com nome inválido: {}", gcsFilePath);
                    return null; // Ignora e passa para o próximo arquivo/partição
                }

                if (iterator == null) {
                    Blob blob = storage.get(bucketName, gcsFilePath);
                    try (InputStream inputStream = Channels.newInputStream(blob.reader())) {
                        Cadoc3044DTO dto = objectMapper.readValue(inputStream, Cadoc3044DTO.class);
                        List<EventoOperacaoContexto> lista = dto.getOperacoes().stream()
                                .map(op -> new EventoOperacaoContexto(
                                        op,
                                        dto.getCnpjIF(),
                                        dto.getDataHoraRemessa(),
                                        dto.getDatabase()
                                ))
                                .toList();
                        iterator = lista.iterator();
                    }
                }
                return iterator.hasNext() ? iterator.next() : null;
            }
        };
    }
}