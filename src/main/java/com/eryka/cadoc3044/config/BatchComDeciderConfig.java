//package com.eryka.cadoc3044.config;
//
//import com.eryka.cadoc3044.decider.NomeArquivoValidoDecider;
//import com.eryka.cadoc3044.dto.EventoOperacaoContexto;
//import com.eryka.cadoc3044.partitioner.GcsFilePartitioner;
//import com.eryka.cadoc3044.writer.Cadoc3044ItemWriter;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.job.builder.FlowBuilder;
//import org.springframework.batch.core.job.builder.JobBuilder;
//import org.springframework.batch.core.job.flow.Flow;
//import org.springframework.batch.core.job.flow.FlowStep;
//import org.springframework.batch.core.repository.JobRepository;
//import org.springframework.batch.core.step.builder.StepBuilder;
//import org.springframework.batch.item.ItemProcessor;
//import org.springframework.batch.item.ItemReader;
//import org.springframework.batch.item.ItemWriter;
//import org.springframework.batch.repeat.RepeatStatus;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.task.SimpleAsyncTaskExecutor;
//import org.springframework.transaction.PlatformTransactionManager;
//
//@Configuration
//public class BatchComDeciderConfig {
//
//    private final JobRepository jobRepository;
//    private final PlatformTransactionManager transactionManager;
//
//    public Cadoc3044BatchConfig(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
//        this.jobRepository = jobRepository;
//        this.transactionManager = transactionManager;
//    }
//
//    @Bean
//    public Job processJsonJob(Step mainStep) {
//        return new JobBuilder("processJsonJob", jobRepository)
//                .start(mainStep)
//                .build();
//    }
//
//    @Bean
//    public ItemWriter<EventoOperacaoContexto> writer() {
//        return new Cadoc3044ItemWriter();
//    }
//
//    @Bean
//    public Step childStep(JobRepository jobRepository,
//                          PlatformTransactionManager transactionManager,
//                          ItemReader<EventoOperacaoContexto> reader,
//                          ItemProcessor<EventoOperacaoContexto, EventoOperacaoContexto> processor) {
//
//        return new StepBuilder("childStep", jobRepository)
//                .<EventoOperacaoContexto, EventoOperacaoContexto>chunk(1, transactionManager)
//                .reader(reader)
//                .processor(processor)
//                .writer(writer())
//                .build();
//    }
//
//    @Bean
//    public Step rejectStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
//        return new StepBuilder("rejectStep", jobRepository)
//                .tasklet((contribution, chunkContext) -> {
//                    System.out.println("Arquivo rejeitado por nome inválido.");
//                    return org.springframework.batch.repeat.RepeatStatus.FINISHED;
//                }, transactionManager)
//                .build();
//    }
//
//    @Bean
//    public Step dummyStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
//        return new StepBuilder("dummyStep", jobRepository)
//                .tasklet((contribution, chunkContext) -> RepeatStatus.FINISHED, transactionManager)
//                .build();
//    }
//
//    @Bean
//    public Flow slaveFlow(NomeArquivoValidoDecider decider, Step dummyStep, Step childStep, Step rejectStep) {
//        return new FlowBuilder<Flow>("slaveFlow")
//                .start(dummyStep)
//                .next(decider)
//                .on("VALIDO").to(childStep)
//                .from(decider).on("INVALIDO").to(rejectStep)
//                .end();
//    }
//
//    @Bean
//    public Step slaveFlowStep(JobRepository jobRepository, @Qualifier("slaveFlow") Flow slaveFlow) {
//        FlowStep flowStep = new FlowStep();
//        flowStep.setName("slaveFlowStep");
//        flowStep.setJobRepository(jobRepository);
//        flowStep.setFlow(slaveFlow);
//        return flowStep;
//    }
//
//    @Bean
//    public Step mainStep(JobRepository jobRepository,
//                         PlatformTransactionManager transactionManager,
//                         GcsFilePartitioner partitioner,
//                         @Qualifier("slaveFlowStep") Step slaveFlowStep) {
//
//        return new StepBuilder("mainStep", jobRepository)
//                .partitioner("slaveFlowStep", partitioner)
//                .step(slaveFlowStep)
//                .taskExecutor(new SimpleAsyncTaskExecutor())
//                .build();
//    }
//}