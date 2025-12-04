package com.example.festival.config;

import com.example.festival.domain.Musical;
import com.example.festival.repository.MusicalRepository;
import com.example.festival.service.MusicalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class BatchConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final MusicalService musicalService;
    private final MusicalRepository musicalRepository;

    private static final int CHUNK_SIZE = 100;

    @Bean
    public Job musicalFetchJob() {
        return new JobBuilder("musicalFetchJob", jobRepository)
                .start(musicalFetchStep())
                .build();
    }

    @Bean
    public Step musicalFetchStep() {
        // 'stepBuilderFactory.get("stepName")' 대신 'new StepBuilder("stepName", jobRepository)' 사용
        return new StepBuilder("musicalFetchStep", jobRepository)
                .<Musical, Musical>chunk(CHUNK_SIZE, transactionManager) // .chunk(size, txManager)
                .reader(musicalPagingItemReader())
                .processor(musicalItemProcessor())
                .writer(musicalItemWriter())
                .build();
    }

    /**
     * ItemReader: KOPIS API 페이징 처리 (이전 버전과 동일)
     */
    @Bean
    @StepScope
    public ItemReader<Musical> musicalPagingItemReader() {


        return new ItemReader<Musical>() {

            private int currentPage = 1;
            private List<Musical> musicalBuffer = new ArrayList<>();
            private int nextIndex = 0;
            private boolean isFinished = false;
            private static final int MAX_ITEMS_TO_FETCH = 300;
            private int totalItemRead;

            @Override
            public Musical read() throws Exception {
                if(totalItemRead == MAX_ITEMS_TO_FETCH) {
                    return null;
                }

                if (isFinished) {
                    return null;
                }

                if (nextIndex >= musicalBuffer.size()) {

                    log.info("Fetching KOPIS data... page={}", currentPage);
                    // MusicalService는 cpage를 받는 fetchMusicals(int cpage)를 사용
                    musicalBuffer = musicalService.fetchMusicalsFromApi(currentPage);

                    if (musicalBuffer == null || musicalBuffer.isEmpty()) {
                        log.info("No more data from KOPIS API. Finishing job.");
                        isFinished = true;
                        return null;
                    }

                    nextIndex = 0;
                    currentPage++;
                }

                Musical nextMusical = musicalBuffer.get(nextIndex);
                nextIndex++;

                return nextMusical;
            }
        };
    }

    /**
     * ItemProcessor: 중복 데이터 처리 (이전 버전과 동일)
     */
    @Bean
    public ItemProcessor<Musical, Musical> musicalItemProcessor() {
        return item -> {
            boolean exists = musicalRepository.existsByMt20id(item.getMt20id());

            if (exists) {
                log.debug("Skipping duplicate item: {} ({})", item.getMt20id(), item.getPrfnm());
                return null; // 중복이면 null 반환 (Writer로 안 감)
            }

            return item; // 신규 데이터면 통과
        };
    }

    /**
     * ItemWriter: 신규 데이터 일괄 저장 (이전 버전과 동일)
     */
    @Bean
    public ItemWriter<Musical> musicalItemWriter() {
        return items -> {
            log.info(">>>>> Writing {} new items to database", items.size());
            if (!items.isEmpty()) {
                musicalRepository.saveAll(items);
            }
        };
    }

}
