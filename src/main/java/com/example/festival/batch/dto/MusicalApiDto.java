package com.example.festival.batch.dto;

import com.example.festival.domain.Musical;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MusicalApiDto {
    private Long mcode; // 디비에서 구분용 고유 id
    private String mt20id; // 뮤지컬 고유 id
    private String prfnm; // 공연 이름
    private String fcltynm; // 공연장명
    private String prfstate; // 공연 상태
    private String poster; // 포스터
    private String prfpdfrom; // 공연 시작일
    private String prfpdto;

    public Musical toEntity() {
        return Musical.builder()
                .mcode(this.mcode)
                .mt20id(this.mt20id)
                .prfnm(this.prfnm)
                .fcltynm(this.fcltynm)
                .prfstate(this.prfstate)
                .poster(this.poster)
                .prfpdfrom(this.prfpdfrom)
                .prfpdto(this.prfpdto)
                .build();
    }
}
