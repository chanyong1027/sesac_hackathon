package com.example.festival.batch.job;

import com.example.festival.batch.dto.MusicalApiDto;
import com.example.festival.batch.processor.MusicalItemProcessor;
import com.example.festival.batch.reader.MusicalItemReader;
import com.example.festival.batch.writer.MusicalItemWriter;
import com.example.festival.domain.Musical;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class MusicalStepConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final MusicalItemReader musicalItemReader;
    private final MusicalItemProcessor musicalItemProcessor;
    private final MusicalItemWriter musicalItemWriter;

    private static final int CHUNK_SIZE = 100;

    @Bean
    public Step musicalFetchStep() {
        return new StepBuilder("musicalFetchStep", jobRepository)
                .<MusicalApiDto, Musical>chunk(CHUNK_SIZE, transactionManager)
                .reader(musicalItemReader)
                .processor(musicalItemProcessor)
                .writer(musicalItemWriter)
                .build();
    }
}
