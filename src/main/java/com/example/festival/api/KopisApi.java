package com.example.festival.api;

import com.example.festival.domain.Musical;
import com.example.festival.dto.KopisMusicalDetailResponseDto;
import com.example.festival.dto.KopisMusicalResponseDto;
import com.example.festival.dto.MusicalDetailResponseDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Component
public class KopisApi {

    private static final int ROWS_PER_PAGE = 100;
    private static final String PRF_STATE_RUNNING = "02";
    private static final String SEOUL_CODE = "11";
    private static final String CATEGORY_MUSICAL = "GGGA";
    private static final String BASE_URL = "http://www.kopis.or.kr";

    @Value("${external.kopis}")
    private String apiKey;
    private final RestClient restClient;

    public KopisApi(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl(BASE_URL)
                .build();
    }

    public List<Musical> fetchMusicals(int cpage){

        try{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            LocalDate now = LocalDate.now();

            String startDate = now.format(formatter);
            String endDate = now.plusMonths(1).format(formatter);

            String uri = UriComponentsBuilder
                    .fromPath("/openApi/restful/pblprfr")
                    .queryParam("service", apiKey)
                    .queryParam("stdate", startDate)
                    .queryParam("eddate", endDate)
                    .queryParam("cpage", cpage)
                    .queryParam("rows", ROWS_PER_PAGE)
                    .queryParam("prfstate", PRF_STATE_RUNNING)
                    .queryParam("signgucode", SEOUL_CODE)
                    .queryParam("shcate", CATEGORY_MUSICAL)
                    .build(true)
                    .toString();

            KopisMusicalResponseDto response= restClient.get()
                    .uri(uri)
                    .retrieve()
                    .body(KopisMusicalResponseDto.class);

            if (response == null || response.getMusicals() == null) {
                return Collections.emptyList();
            }

            return response.getMusicals();
        }
        catch (RestClientException e){
            log.error("Kopis api 호출 중 오류 발생: cpage={}", cpage, e);
            return Collections.emptyList();
        }
    }

    public Optional<MusicalDetailResponseDto> fetchMusicalDetail(String mt20id) {

        try {
            String uri = UriComponentsBuilder
                    .fromPath("/openApi/restful/pblprfr/{mt20id}")
                    .queryParam("service", apiKey)
                    .buildAndExpand(mt20id)
                    .toString();

            KopisMusicalDetailResponseDto response = restClient.get()
                    .uri(uri)
                    .retrieve()
                    .body(KopisMusicalDetailResponseDto.class);

            if (response == null || response.getDetail() == null || response.getDetail().isEmpty()) {
                return Optional.empty();
            }

            return Optional.ofNullable(response.getDetail().get(0));

        } catch (RestClientException e) {
            log.error("KOPIS 상세 API 호출 및 파싱 중 오류 발생: mt20id={}", mt20id, e);
            return Optional.empty();
        }
    }
}
