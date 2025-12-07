package com.example.festival.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BatchTestController {

    private final JobLauncher jobLauncher;
    private final Job musicalFetchJob;

    /**
     * http://localhost:8080/run-job
     * 위 URL을 브라우저에서 GET 요청하면 musicalFetchJob을 실행시킵니다.
     */
    @GetMapping("/run-job")
    public String runJob() {
        try {
            // *** (매우 중요) Job Parameter ***
            // Spring Batch는 동일한 Job Parameter로 Job을 두 번 실행하지 않습니다.
            // (JobInstanceAlreadyCompleteException 발생)
            // 테스트를 위해 매번 다른 Parameter를 주어야 하므로, 현재 시간을 파라미터로 넘깁니다.
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("run.datetime", LocalDateTime.now().toString())
                    .toJobParameters();

            log.info(">>>>> Spring Batch Job(musicalFetchJob) 수동 실행 시작");

            // jobLauncher를 통해 Job을 실행시킵니다.
            jobLauncher.run(musicalFetchJob, jobParameters);

            log.info(">>>>> Spring Batch Job(musicalFetchJob) 실행 완료");

            return "Batch Job(musicalFetchJob) Executed Successfully!";

        } catch (Exception e) {
            log.error("Batch Job 실행 중 오류 발생", e);
            return "Error executing job: " + e.getMessage();
        }
    }
}
