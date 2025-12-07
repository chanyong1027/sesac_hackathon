package com.example.festival.batch.client;

import com.example.festival.batch.dto.MusicalApiDto;
import java.util.List;

public interface MusicalDataProvider {
    List<MusicalApiDto> fetchMusicals(int page);
}
