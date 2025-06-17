package com.eryka.cadoc3044.service;

import com.google.cloud.pubsub.v1.Publisher;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.ProjectTopicName;
import com.google.pubsub.v1.PubsubMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PubSubServiceImpl {

    @Value("${gcp.project-id}")
    private String projectId;

    @Value("${gcp.pubsub.topic}")
    private String topicId;

    public void publicarRejeicao(String json, String dsErro) {
        Publisher publisher = null;
        try {
            ProjectTopicName topicName = ProjectTopicName.of(projectId, topicId);

            publisher = Publisher.newBuilder(topicName).build();

            String payload = String.format("{\"json\":%s,\"dsErro\":\"%s\"}", json, dsErro);

            PubsubMessage pubsubMessage = PubsubMessage.newBuilder()
                    .setData(ByteString.copyFromUtf8(payload))
                    .build();

            publisher.publish(pubsubMessage).get();
            log.info("Mensagem publicada no Pub/Sub com sucesso.");

        } catch (Exception e) {
            log.error("Erro ao publicar no Pub/Sub: {}", e.getMessage(), e);
        } finally {
            if (publisher != null) {
                try {
                    publisher.shutdown();
                } catch (Exception ex) {
                    log.warn("Erro ao fechar o publisher do Pub/Sub: {}", ex.getMessage());
                }
            }
        }
    }
}