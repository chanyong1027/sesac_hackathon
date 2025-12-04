package com.example.festival.controller;

import com.example.festival.dto.MusicalDetailResponseDto;
import com.example.festival.dto.MusicalResponseDto;
import com.example.festival.service.MusicalService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MusicalController {
    private final MusicalService musicalService;

    @GetMapping("/musicals")
    public ResponseEntity<Page<MusicalResponseDto>> getMusicals(Pageable pageable) {
        Page<MusicalResponseDto> musicalPage = musicalService.findMusicalsFromDb(pageable);
        return ResponseEntity.ok(musicalPage);
    }

    @GetMapping("/musicals/{mt20id}")
    public ResponseEntity<MusicalDetailResponseDto> getMusicalDetails(@PathVariable("mt20id") String mt20id) {
        MusicalDetailResponseDto musicalDetailResponseDto = musicalService.getMusicalDetail(mt20id);
        return ResponseEntity.ok(musicalDetailResponseDto);
    }
}
