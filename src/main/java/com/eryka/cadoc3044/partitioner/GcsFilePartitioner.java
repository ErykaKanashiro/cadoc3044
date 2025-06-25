package com.eryka.cadoc3044.partitioner;


import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class GcsFilePartitioner implements Partitioner {

    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;

    private final Storage storage;

    public GcsFilePartitioner(Storage storage) {
        this.storage = storage;
    }

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {

        Map<String, ExecutionContext> partitions = new HashMap<>();
        int index = 1;

        Page<Blob> blobs = storage.list(bucketName);

        for (Blob blob : blobs.iterateAll()) {
            String fileName = blob.getName();
            if (fileName.endsWith(".json")) {
                ExecutionContext context = new ExecutionContext();
                context.putString("fileName", fileName);
                context.putString("bucketName", bucketName);
                partitions.put("partition_" + index++, context);
            }
        }
        return partitions;
    }
}
