package com.eryka.cadoc3044.reader;

import com.eryka.cadoc3044.dto.Cadoc3044DTO;
import com.eryka.cadoc3044.dto.EventoOperacaoDTO;
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
public class Cadoc3044FileReader implements ItemReader<EventoOperacaoDTO> {

    private final ResourceLoader resourceLoader;
    private final ObjectMapper objectMapper;

    private List<EventoOperacaoDTO> eventosCredito;
    private int currentIndex = 0;

    private final String gcsFilePath = "gs://seu-bucket/nome-do-arquivo.json";
    private final String localFilePath = "src/main/resources/cadoc3044_exemplo.json";

    public Cadoc3044FileReader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public EventoOperacaoDTO read() throws Exception {
        if (eventosCredito == null) {
            InputStream inputStream;

            boolean useLocalFile = true;

            if (useLocalFile) {
                File file = new File(localFilePath);
                inputStream = new FileInputStream(file);
            } else {
                Resource resource = resourceLoader.getResource(gcsFilePath);
                inputStream = resource.getInputStream();
            }

            // Ler o JSON raiz(root) para Cadoc3044.class
            Cadoc3044DTO cadoc3044DTO = objectMapper.readValue(inputStream, Cadoc3044DTO.class);

            eventosCredito = cadoc3044DTO.getOperacoes();

        }

        if (currentIndex < eventosCredito.size()) {
            return eventosCredito.get(currentIndex++);

        } else {
            return null;

        }
    }
}
//package com.eryka.cadoc3044.reader;
//
//import com.eryka.cadoc3044.model.EventoOperacao;
//import com.eryka.cadoc3044.model.Cadoc3044;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.google.cloud.storage.Blob;
//import com.google.cloud.storage.Bucket;
//import com.google.cloud.storage.Storage;
//import com.google.cloud.storage.StorageOptions;
//import org.springframework.batch.item.ItemReader;
//import org.springframework.stereotype.Component;
//
//import java.io.FileInputStream;
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//
//@Component
//public class Cadoc3044FileReader implements ItemReader<EventoOperacao> {
//
//    private final ObjectMapper objectMapper = new ObjectMapper();
//    private final Storage storage = StorageOptions.getDefaultInstance().getService();
//    private final String bucketName = "SEU_BUCKET"; // Substitua pelo nome do seu bucket
//    private final String localFilePath = "src/main/resources/cadoc3044_exemplo.json"; // Caminho do arquivo local
//    private final boolean useLocalFile = true; // Troque para false quando for usar o bucket
//
//    private Iterator<Blob> blobIterator;
//    private Iterator<EventoOperacao> eventoIterator;
//    private boolean localFileRead = false;
//
//    public Cadoc3044FileReader() {}
//
//    private void initBlobIterator() {
//        if (blobIterator == null) {
//            Bucket bucket = storage.get(bucketName);
//            List<Blob> blobs = new ArrayList<>();
//            for (Blob blob : bucket.list().iterateAll()) {
//                if (blob.getName().endsWith(".json")) {
//                    blobs.add(blob);
//                }
//            }
//            blobIterator = blobs.iterator();
//        }
//    }
//
//    @Override
//    public EventoOperacao read() throws Exception {
//        if (useLocalFile) {
//            if (localFileRead) return null;
//            try (InputStream inputStream = new FileInputStream(localFilePath)) {
//                Cadoc3044 cadoc3044 = objectMapper.readValue(inputStream, Cadoc3044.class);
//                List<EventoOperacao> eventos = cadoc3044.getOperacoes();
//                if (eventos != null && !eventos.isEmpty()) {
//                    eventoIterator = eventos.iterator();
//                } else {
//                    eventoIterator = null;
//                }
//            }
//            localFileRead = true;
//        } else {
//            initBlobIterator();
//            while ((eventoIterator == null || !eventoIterator.hasNext()) && blobIterator.hasNext()) {
//                Blob blob = blobIterator.next();
//                try (InputStream inputStream = new java.io.ByteArrayInputStream(blob.getContent())) {
//                    Cadoc3044 cadoc3044 = objectMapper.readValue(inputStream, Cadoc3044.class);
//                    List<EventoOperacao> eventos = cadoc3044.getOperacoes();
//                    if (eventos != null && !eventos.isEmpty()) {
//                        eventoIterator = eventos.iterator();
//                    } else {
//                        eventoIterator = null;
//                    }
//                }
//            }
//        }
//
//        if (eventoIterator != null && eventoIterator.hasNext()) {
//            return eventoIterator.next();
//        } else {
//            return null;
//        }
//    }
//}