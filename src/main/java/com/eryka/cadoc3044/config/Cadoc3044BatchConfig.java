package com.eryka.cadoc3044.config;

import com.eryka.cadoc3044.dto.EventoOperacaoDTO;
import com.eryka.cadoc3044.processor.Cadoc3044Processor;
import com.eryka.cadoc3044.reader.Cadoc3044FileReader;
import com.eryka.cadoc3044.writer.Cadoc3044ItemWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
    public Job processJsonJob(JobRepository jobRepository, Step processStep) {
        return new JobBuilder("processJsonJob", jobRepository)
                .start(processStep)
                .build();
    }

    @Bean
    public Step processStep(JobRepository jobRepository,
                            PlatformTransactionManager transactionManager,
                            Cadoc3044FileReader reader,
                            Cadoc3044Processor processor) {

        return new StepBuilder("step1", jobRepository)
                .<EventoOperacaoDTO, EventoOperacaoDTO>chunk(1, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer())
                .build();
    }

    @Bean
    public ItemWriter<EventoOperacaoDTO> writer() {
        return new Cadoc3044ItemWriter();
    }

}
