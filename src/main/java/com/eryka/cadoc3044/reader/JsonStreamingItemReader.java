package com.eryka.cadoc3044.reader;

import com.eryka.cadoc3044.model.Operacao;
import com.eryka.cadoc3044.model.PagAgregado;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;

@Slf4j
@Component
public class JsonStreamingItemReader implements ItemReader<Object> {

    private final ObjectMapper objectMapper;
    private JsonParser parser;
    private boolean lendoOperacoes = false;
    private boolean lendoPagAgregados = false;
    private boolean fimDoArquivo = false;
    private String dataHoraRemessa = null;
    private String cdOrigem = null;

    public JsonStreamingItemReader(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Object read() throws Exception {
        if (fimDoArquivo) return null;

        if (parser == null) {
            File file = new File("I:/eryka/Downloads/cadoc3044/src/main/resources/cadoc3044_exemplo_2.json");
            parser = new JsonFactory().createParser(new FileInputStream(file));
        }

        while (!parser.isClosed()) {
            JsonToken token = parser.nextToken();

            if (token == null) {
                fimDoArquivo = true;
                parser.close();
                return null;
            }

            if (JsonToken.FIELD_NAME.equals(token)) {
                if ("operacoes".equals(parser.getCurrentName())) {
                    lendoOperacoes = true;
                    lendoPagAgregados = false;
                    parser.nextToken();
                } else if ("pagAgregados".equals(parser.getCurrentName())) {
                    lendoOperacoes = false;
                    lendoPagAgregados = true;
                    parser.nextToken();
                }
            }

            if (JsonToken.START_OBJECT.equals(token)) {
                if (lendoOperacoes) {
                    Operacao operacao = objectMapper.readValue(parser, Operacao.class);
                    log.info("Lido Operacao: {}", operacao);
                    return operacao;
                }

                if (lendoPagAgregados) {
                    PagAgregado pagAgregado = objectMapper.readValue(parser, PagAgregado.class);
                    log.info("Lido PagAgregado: {}", pagAgregado);
                    return pagAgregado;
                }
            }
        }

        fimDoArquivo = true;
        parser.close();
        return null;
    }
}
