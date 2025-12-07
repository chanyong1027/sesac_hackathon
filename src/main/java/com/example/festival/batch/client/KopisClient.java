package com.example.festival.batch.client;

import com.example.festival.api.KopisApi;
import com.example.festival.batch.dto.MusicalApiDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class KopisClient implements MusicalDataProvider {

    private final KopisApi kopisApi;

    @Override
    public List<MusicalApiDto> fetchMusicals(int page) {
        return kopisApi.fetchMusicals(page);
    }
}
