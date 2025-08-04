package com.example.shelter.weather;

import com.example.shelter.weather.dto.WeatherDto;
import com.example.shelter.weather.repository.WeatherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WeatherService {
    private final WeatherRepository repository;
    private final TranNxNyService translator;
    private final WeatherMapper mapper;


    public WeatherDto findTodayWeather(String userLat, String userLot){
        Grid loc = translator.latLonToGrid(Double.parseDouble(userLat),Double.parseDouble(userLot));
        List<Weather> entity =repository.findAllByNxBetweenAndNyBetween(loc.getNx()-1, loc.getNx()+1,loc.getNy()-1,loc.getNy()+1);
        if (entity.isEmpty()) return null;
        else if (entity.size()==1) return mapper.toDto(entity.getFirst());
        else {
            Weather res = findNearestWeather(entity,Double.parseDouble(userLat),Double.parseDouble(userLot));
            return mapper.toDto(res);
        }
    }

    private Weather findNearestWeather(List<Weather> candidates, double userLat, double userLon) {
        return candidates.stream()
                .min(Comparator.comparing(weather -> {
                    Grid grid = translator.gridToLatLon(weather.getNx(), weather.getNy());
                    return haversineDistance(userLat, userLon, grid.getLat(), grid.getLot());
                }))
                .orElse(null);
    }

    private double haversineDistance(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371e3; // Earth radius in meters
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }


}
