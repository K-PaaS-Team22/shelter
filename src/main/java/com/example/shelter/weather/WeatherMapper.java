package com.example.shelter.weather;

import com.example.shelter.weather.dto.WeatherDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WeatherMapper {

    @Mapping(target="id", ignore=true)
    Weather toEntity(WeatherDto dto);

    WeatherDto toDto(Weather entity);

    List<WeatherDto> toDtoList(List<Weather> entityList);
    @Mapping(target="id", ignore=true)
    List<Weather> toEntityList(List<WeatherDto> dtoList);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(WeatherDto dto, @MappingTarget Weather entity);
}
