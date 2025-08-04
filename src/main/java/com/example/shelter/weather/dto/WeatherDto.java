package com.example.shelter.weather.dto;

import lombok.Data;

@Data
public class WeatherDto {
    private String baseDate;
    private String baseTime;
    private String fcstTime;
    private String locationName;
    private Integer nx;
    private Integer ny;
    private String tmp;   // 강수형태
    private String uuu;   // 습도
    private String vvv;   // 1시간 강수량
    private String vec;   // 기온
    private String wsd;   // 동서바람성분
    private String sky;   // 풍향
    private String pty;   // 남북바람성분
    private String pop;   // 풍속
    private String wav;   // 풍속
    private String pcp;   // 풍속

}
