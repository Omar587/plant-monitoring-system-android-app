package com.example.cmpt496;

public class TowerModel {
    String towerName;
    String date;
    String plantStage;
    String plantName;
    String col_id;


    public TowerModel(String towerName, String date, String plantStage, String plantName, String col_id) {
        this.towerName = towerName;
        this.date = date;
        this.plantStage = plantStage;
        this.plantName = plantName;
        this.col_id = col_id;
    }

    public String getTowerName() {
        return towerName;
    }

    public String getDate() {
        return date;
    }

    public String getPlantStage() {
        return plantStage;
    }

    public String getPlantName() {
        return plantName;
    }

    public String getCol_id() {
        return col_id;
    }
}
