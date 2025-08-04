package com.example.shelter.kafka;

import com.example.shelter.shelter.SummerShelterMapper;
import com.example.shelter.shelter.WinterShelterMapper;
import com.example.shelter.shelter.dto.SummerShelterDto;
import com.example.shelter.shelter.dto.WinterShelterDto;
import com.example.shelter.shelter.repository.SummerShelterRepository;
import com.example.shelter.shelter.repository.WinterShelterRepository;
import com.example.shelter.weather.Weather;
import com.example.shelter.weather.dto.WeatherDto;
import com.example.shelter.weather.WeatherMapper;
import com.example.shelter.weather.repository.WeatherRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {
    private final SummerShelterRepository summerRepo;
    private final WinterShelterRepository winterRepo;
    private final WeatherRepository weatherRepo;
    private final SummerShelterMapper summerShelterMapper;
    private final WinterShelterMapper winterShelterMapper;
    private final WeatherMapper weatherMapper;


    @KafkaListener(topics = "summer-shelter-data", groupId = "shelter-group")
    public void consumeSummer(List<LinkedHashMap<String, Object>> data) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<SummerShelterDto> dtoList = data.stream()
                .map(map -> objectMapper.convertValue(map, SummerShelterDto.class))
                .collect(Collectors.toList());
        if (!data.isEmpty()) summerRepo.saveAll(summerShelterMapper.toEntityList(dtoList));
    }

    @KafkaListener(topics = "winter-shelter-data", groupId = "shelter-group")
    public void consumeWinter(List<LinkedHashMap<String, Object>> data) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<WinterShelterDto> dtoList = data.stream()
                .map(map -> objectMapper.convertValue(map, WinterShelterDto.class))
                .collect(Collectors.toList());
        if (!data.isEmpty()) winterRepo.saveAll(winterShelterMapper.toEntityList(dtoList));
    }

    @KafkaListener(topics = "weather-data", groupId = "weather-group")
    public void consumeWeather(List<LinkedHashMap<String, Object>> data) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<WeatherDto> dtoList = data.stream()
                .map(map -> objectMapper.convertValue(map, WeatherDto.class))
                .toList();
        if (!data.isEmpty()) {
            for (WeatherDto dto : dtoList) saveOrUpdateByLocationName(dto);
        }
    }
    public void saveOrUpdateByLocationName(WeatherDto dto) {
        Weather entity = weatherRepo.findByLocationName(dto.getLocationName())
                .orElse(new Weather());

        if (entity.getId() == null) {
            entity.setLocationName(dto.getLocationName());
        }

        weatherMapper.updateEntityFromDto(dto,entity);
        weatherRepo.save(entity);
    }
}
