package com.eryka.cadoc3044.reader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.eryka.cadoc3044.model.Evento;
import org.springframework.batch.item.ItemReader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

@Component
public class JsonFileReader implements ItemReader<Evento> {

    private final ResourceLoader resourceLoader;
    private final ObjectMapper objectMapper;

    private List<Evento> eventos;
    private int currentIndex = 0;

    // Caminho GCP (prefixo gs://) ou local para testes
    private final String gcsFilePath = "gs://seu-bucket/nome-do-arquivo.json";
    private final String localFilePath = "I:/eryka/Downloads/cadoc3044/src/main/resources/cadoc3044_exemplo.json";

    public JsonFileReader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public Evento read() throws Exception {
        if (eventos == null) {
            InputStream inputStream;

            // ======== TROCAR ESTA FLAG CONFORME O AMBIENTE ========
            boolean useLocalFile = true;
            // =====================================================

            if (useLocalFile) {
                File file = new File(localFilePath);
                inputStream = new FileInputStream(file);
            } else {
                Resource resource = resourceLoader.getResource(gcsFilePath);
                inputStream = resource.getInputStream();
            }

            eventos = objectMapper.readValue(inputStream,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Evento.class));
        }

        if (currentIndex < eventos.size()) {
            return eventos.get(currentIndex++);
        } else {
            return null;
        }
    }
}
