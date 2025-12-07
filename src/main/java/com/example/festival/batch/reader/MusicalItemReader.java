package com.example.festival.batch.reader;

import com.example.festival.batch.client.MusicalDataProvider;
import com.example.festival.batch.dto.MusicalApiDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@StepScope
@RequiredArgsConstructor
@Slf4j
public class MusicalItemReader implements ItemReader<MusicalApiDto> {
    private final MusicalDataProvider  musicalDataProvider;

    private static final int MAX_ITEMS_TO_FETCH = 300;

    private int currentPage = 1;
    private List<MusicalApiDto> musicalBuffer = new ArrayList<>();
    private int nextIndex = 0;
    private boolean isFinished = false;
    private int totalItemRead;

    @Override
    public MusicalApiDto read() {
        if(totalItemRead == MAX_ITEMS_TO_FETCH) {
            return null;
        }

        if (isFinished) {
            return null;
        }

        if (nextIndex >= musicalBuffer.size()) {

            log.info("kopis 데이터들을 가져오는 중={}", currentPage);
            musicalBuffer = musicalDataProvider.fetchMusicals(currentPage);

            if (musicalBuffer == null || musicalBuffer.isEmpty()) {
                log.info("더 가져올 kopis 데이터가 없습니다.");
                isFinished = true;
                return null;
            }

            nextIndex = 0;
            currentPage++;
        }

        MusicalApiDto nextMusical = musicalBuffer.get(nextIndex);
        nextIndex++;
        totalItemRead++;

        return nextMusical;
    }
}
