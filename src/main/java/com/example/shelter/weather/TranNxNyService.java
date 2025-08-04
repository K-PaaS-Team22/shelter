package com.example.shelter.weather;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TranNxNyService {
    private static final double RE = 6371.00877;    // 지구 반경(km)
    private static final double GRID = 5.0;         // 격자 간격(km)
    private static final double SLAT1 = 30.0;       // 투영 위도1(degree)
    private static final double SLAT2 = 60.0;       // 투영 위도2(degree)
    private static final double OLON = 126.0;       // 기준점 경도(degree)
    private static final double OLAT = 38.0;        // 기준점 위도(degree)
    private static final double XO = 43;            // 기준점 X좌표 (GRID 기준)
    private static final double YO = 136;           // 기준점 Y좌표 (GRID 기준)

    private static final double DEGRAD = Math.PI / 180.0;
    private static final double RADDEG = 180.0 / Math.PI;

    private static double re, slat1, slat2, olon, olat, xo, yo;
    private static double sn, sf, ro;

    static {
        re = RE / GRID;
        slat1 = SLAT1 * DEGRAD;
        slat2 = SLAT2 * DEGRAD;
        olon = OLON * DEGRAD;
        olat = OLAT * DEGRAD;

        sn = Math.tan(Math.PI * 0.25 + slat2 * 0.5) / Math.tan(Math.PI * 0.25 + slat1 * 0.5);
        sn = Math.log(Math.cos(slat1) / Math.cos(slat2)) / Math.log(sn);
        sf = Math.tan(Math.PI * 0.25 + slat1 * 0.5);
        sf = Math.pow(sf, sn) * Math.cos(slat1) / sn;
        ro = Math.tan(Math.PI * 0.25 + olat * 0.5);
        ro = re * sf / Math.pow(ro, sn);

        xo = XO;
        yo = YO;
    }

    // 위경도 -> 격자 (L → G)
    public Grid latLonToGrid(double lat, double lon) {
        double ra = Math.tan(Math.PI * 0.25 + (lat * DEGRAD) * 0.5);
        ra = re * sf / Math.pow(ra, sn);
        double theta = lon * DEGRAD - olon;

        if (theta > Math.PI) theta -= 2.0 * Math.PI;
        if (theta < -Math.PI) theta += 2.0 * Math.PI;

        theta *= sn;

        int x = (int)(Math.floor(ra * Math.sin(theta) + xo + 0.5));
        int y = (int)(Math.floor(ro - ra * Math.cos(theta) + yo + 0.5));

        return new Grid(x, y);
    }
    public Grid gridToLatLon(int x, int y) {
        double re = 6371.00877 / 5.0;     // Earth radius / grid spacing
        double slat1 = 30.0 * DEGRAD;
        double slat2 = 60.0 * DEGRAD;
        double olon = 126.0 * DEGRAD;
        double olat = 38.0 * DEGRAD;
        double xo = 43;
        double yo = 136;

        double sn = Math.tan(Math.PI * 0.25 + slat2 / 2) / Math.tan(Math.PI * 0.25 + slat1 / 2);
        sn = Math.log(Math.cos(slat1) / Math.cos(slat2)) / Math.log(sn);
        double sf = Math.tan(Math.PI * 0.25 + slat1 / 2);
        sf = Math.pow(sf, sn) * Math.cos(slat1) / sn;
        double ro = Math.tan(Math.PI * 0.25 + olat / 2);
        ro = re * sf / Math.pow(ro, sn);

        double xn = x - xo;
        double yn = ro - (y - yo);
        double ra = Math.sqrt(xn * xn + yn * yn);
        double alat = Math.pow((re * sf / ra), (1.0 / sn));
        alat = 2 * Math.atan(alat) - Math.PI * 0.5;

        double theta = 0.0;
        if (Math.abs(xn) <= 1e-7) {
            theta = 0.0;
        } else {
            if (Math.abs(yn) <= 1e-7) {
                theta = Math.PI * 0.5;
                if (xn < 0.0) theta = -theta;
            } else {
                theta = Math.atan2(xn, yn);
            }
        }

        double alon = theta / sn + olon;
        Grid grid = new Grid();
        grid.setLat(alat * RADDEG);
        grid.setLot(alon * RADDEG);
        return grid;
    }

}
