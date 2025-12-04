package com.example.festival.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MusicalDetailResponseDto {
    private String mt20id; // 뮤지컬 고유 id
    private String prfnm; // 공연 이름
    private String fcltynm; // 공연장명
    private String prfstate; // 공연 상태
    private String poster; // 포스터
    private String prfpdfrom; // 공연 시작일
    private String prfpdto; // 공연 종료일
    private String prfcast; // 출연진
    private String prfruntime; // 공연 런타임
    private String prfage; // 연령 제한
    private String entrpsnmP; // 제작사
    private String pcseguidance; //티켓 가격
    private String sty; // 줄거리
    private String dtguidance; // 공연시간
}
