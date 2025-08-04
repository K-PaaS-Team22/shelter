package com.example.shelter.weather.repository;

import com.example.shelter.weather.Weather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface WeatherRepository extends JpaRepository<Weather,Long> {
    //public List<WinterShelter> findByRegionCode(String regionCode);
    public Weather findByNxAndNy(Integer nx, Integer ny);
    public List<Weather> findAllByNxBetweenAndNyBetween(Integer nx1, Integer nx2, Integer ny1, Integer ny2);
    public Optional<Weather> findByLocationName(String locationName);

}
