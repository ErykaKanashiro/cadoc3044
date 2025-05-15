package com.eryka.cadoc3044.reader;

import com.eryka.cadoc3044.model.Evento;
import com.eryka.cadoc3044.model.Root;
import com.fasterxml.jackson.databind.ObjectMapper;
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

            boolean useLocalFile = true;

            if (useLocalFile) {
                File file = new File(localFilePath);
                inputStream = new FileInputStream(file);
            } else {
                Resource resource = resourceLoader.getResource(gcsFilePath);
                inputStream = resource.getInputStream();
            }

            // Ler o JSON raiz para Root.class
            Root root = objectMapper.readValue(inputStream, Root.class);

            eventos = root.getOperacoes();
        }

        if (currentIndex < eventos.size()) {
            return eventos.get(currentIndex++);
        } else {
            return null;
        }
    }
}
