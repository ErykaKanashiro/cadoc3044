package com.eryka.cadoc3044.config;

import com.eryka.cadoc3044.model.Evento;
import com.eryka.cadoc3044.processor.EventoProcessor;
import com.eryka.cadoc3044.reader.JsonFileReader;
import com.eryka.cadoc3044.writer.EventoItemWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Bean
    public Job processJsonJob(JobLauncher jobLauncher, JsonFileReader reader, EventoProcessor processor) {

        Step step = new StepBuilder("step1")
                .<Evento, Evento>chunk(10)
                .reader(reader)
                .processor(processor)
                .writer(writer())
                .build();

        return new JobBuilder("processJsonJob")
                .start(step)
                .build();
    }

    @Bean
    public ItemWriter<Evento> writer() {
        return new EventoItemWriter();
    }

}
