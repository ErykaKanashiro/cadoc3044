package com.eryka.cadoc3044.decider;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.stereotype.Component;

@Component
public class NomeArquivoValidoDecider implements JobExecutionDecider {
    @Override
    public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
        if (stepExecution == null) {
            return new FlowExecutionStatus("INVALIDO");
        }
        String nomeArquivo = stepExecution.getExecutionContext().getString("fileName", null);
        if (nomeArquivo.matches("cadoc3044_exemplo.json")) {
            return new FlowExecutionStatus("VALIDO");
        }
        return new FlowExecutionStatus("INVALIDO");
    }
}