package com.example.festival.dto;

import com.example.festival.domain.Musical;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MusicalResponseDto {
    private String mt20id;
    private String prfnm; //공연 이름
    private String fcltynm; // 공연장 이름
    private String poster; // poster
    private String prfpdfrom; // 공연 시작일
    private String prfpdto; // 공연 종료일

    public MusicalResponseDto(Musical musical) {
        this.mt20id = musical.getMt20id();
        this.prfnm = musical.getPrfnm();
        this.fcltynm = musical.getFcltynm();
        this.poster = musical.getPoster();
        this.prfpdfrom = musical.getPrfpdfrom();
        this.prfpdto = musical.getPrfpdto();
    }
}
