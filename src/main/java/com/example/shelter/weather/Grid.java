package com.example.shelter.weather;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Grid {
    private int nx;
    private int ny;
    private double lat;
    private double lot;
    public Grid(int nx, int ny){
        this.nx=nx;
        this.ny=ny;
    }
}
