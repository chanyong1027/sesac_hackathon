package com.example.festival.batch.processor;

import com.example.festival.batch.dto.MusicalApiDto;
import com.example.festival.domain.Musical;
import com.example.festival.repository.MusicalRepository;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MusicalItemProcessor implements ItemProcessor<MusicalApiDto, Musical> {

    private final MusicalRepository musicalRepository;

    @Override
    public Musical process(@Nonnull MusicalApiDto item) {

        if (isDuplicate(item)) {
            log.debug("중복 아이템 스킵: {} ({})",
                    item.getMt20id(), item.getPrfnm());
            return null;
        }

        log.debug("새로운 아이템 처리: {} ({})",
                item.getMt20id(), item.getPrfnm());
        return item.toEntity();
    }

    private boolean isDuplicate(MusicalApiDto item) {
        return musicalRepository.existsByMt20id(item.getMt20id());
    }
}
