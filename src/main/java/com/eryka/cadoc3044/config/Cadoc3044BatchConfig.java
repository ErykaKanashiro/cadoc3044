package com.eryka.cadoc3044.config;

import com.eryka.cadoc3044.dto.EventoOperacaoContexto;
import com.eryka.cadoc3044.partitioner.GcsFilePartitioner;
import com.eryka.cadoc3044.writer.Cadoc3044ItemWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class Cadoc3044BatchConfig {

    private JobRepository jobRepository;
    private PlatformTransactionManager transactionManager;

    public Cadoc3044BatchConfig(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

    @Bean
    public Job processJsonJob(Step mainStep) {
        return new JobBuilder("processJsonJob", jobRepository)
                .start(mainStep)
                .build();
    }

    @Bean
    public ItemWriter<EventoOperacaoContexto> writer() {
        return new Cadoc3044ItemWriter();
    }

    @Bean
    public Step childStep(JobRepository jobRepository,
                          PlatformTransactionManager transactionManager,
                          ItemReader reader,
                          ItemProcessor processor) {

        return new StepBuilder("childStep", jobRepository)
                .<EventoOperacaoContexto, EventoOperacaoContexto>chunk(1, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer())
                .build();
    }

    @Bean
    public Step mainStep(JobRepository jobRepository,
                          PlatformTransactionManager transactionManager,
                          GcsFilePartitioner partitioner,
                          Step slaveStep) {

        return new StepBuilder("mainStep", jobRepository)
                .partitioner("childStep", partitioner)
                .step(slaveStep)
                .taskExecutor(new SimpleAsyncTaskExecutor())
                .build();
    }
}
