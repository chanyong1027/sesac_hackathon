package com.example.festival.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Getter
public class Musical {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mcode")
    private Long mcode; // 디비에서 구분용 고유 id
    private String mt20id; // 뮤지컬 고유 id
    private String prfnm; // 공연 이름
    private String fcltynm; // 공연장명
    private String prfstate; // 공연 상태
    private String poster; // 포스터
    private String prfpdfrom; // 공연 시작일
    private String prfpdto; // 공연 종료일
}
