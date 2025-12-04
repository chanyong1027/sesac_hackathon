package com.example.festival.service;

import com.example.festival.api.KopisApi;
import com.example.festival.domain.Musical;
import com.example.festival.dto.MusicalDetailResponseDto;
import com.example.festival.dto.MusicalResponseDto;
import com.example.festival.repository.MusicalRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MusicalService {
    private final KopisApi kopisApi;
    private final MusicalRepository musicalRepository;

    public Page<MusicalResponseDto> findMusicalsFromDb(Pageable pageable) {
        Page<Musical> musicalPage = musicalRepository.findAll(pageable);
        return musicalPage.map(MusicalResponseDto::new);
    }

    public List<Musical> fetchMusicalsFromApi(int cpage) {
        return kopisApi.fetchMusicals(cpage);
    }

    public MusicalDetailResponseDto getMusicalDetail(String mt20id) {
        MusicalDetailResponseDto musicalDetailResponseDto = kopisApi.fetchMusicalDetail(mt20id);
        if (musicalDetailResponseDto == null) {
            // TODO: ID에 해당하는 뮤지컬이 없을 때 예외 처리
             throw new EntityNotFoundException("뮤지컬 정보를 찾을 수 없습니다.");
        }
        return musicalDetailResponseDto;
    }
}
