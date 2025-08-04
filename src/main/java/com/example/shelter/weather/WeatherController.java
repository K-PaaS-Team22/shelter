package com.example.shelter.weather;

import com.example.shelter.weather.dto.WeatherDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/weather")
@RequiredArgsConstructor
public class WeatherController {
    private final WeatherService weatherService;

    @GetMapping("/today")
    @Operation(summary = "오늘 날씨 조회", description = "오늘의 날씨를 기상청에서 조회",
            parameters = {
                    @Parameter(in = ParameterIn.HEADER, name = "userLat", description = "사용자 위치 위도", required = true, example = "37.359708"),
                    @Parameter(in = ParameterIn.HEADER, name = "userLot", description = "사용자 위치 경도", required = true, example = "127.1058342")
            })
    public WeatherDto getWeather(@RequestParam(value = "userLat",required = true) String userLat,
                                 @RequestParam(value = "userLot",required = true)String userLot){
        return weatherService.findTodayWeather(userLat,userLot);
    }
}
