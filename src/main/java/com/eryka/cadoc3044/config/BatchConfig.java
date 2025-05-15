package com.eryka.cadoc3044.config;

import com.eryka.cadoc3044.model.Evento;
import com.eryka.cadoc3044.processor.EventoProcessor;
import com.eryka.cadoc3044.reader.JsonFileReader;
import com.eryka.cadoc3044.writer.EventoItemWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BatchConfig {

    private JobRepository jobRepository;
    private PlatformTransactionManager transactionManager;

    public BatchConfig(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

    @Bean
    public Job processJsonJob(JobRepository jobRepository,
                              Step processStep) {
        return new JobBuilder("processJsonJob", jobRepository)
                .start(processStep)
                .build();
    }

    @Bean
    public Step processStep(JobRepository jobRepository,
                            PlatformTransactionManager transactionManager,
                            JsonFileReader reader,
                            EventoProcessor processor) {

        return new StepBuilder("step1", jobRepository)
                .<Evento, Evento>chunk(1, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer())
                .build();
    }

    @Bean
    public ItemWriter<Evento> writer() {
        return new EventoItemWriter();
    }

}
