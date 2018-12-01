package com.example.android.fittracker.data;

import java.util.ArrayList;
import java.util.List;

public class HistoryData {

    private String date;
    private double duration_walking;
    private double duration_running;
    private double duration_cycling;
    private double calorie_walking;
    private double calorie_running;
    private double calorie_cycling;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getDuration_walking() {
        return duration_walking;
    }

    public void setDuration_walking(double duration_walking) {
        this.duration_walking = duration_walking;
    }

    public double getDuration_running() {
        return duration_running;
    }

    public void setDuration_running(double duration_running) {
        this.duration_running = duration_running;
    }

    public double getDuration_cycling() {
        return duration_cycling;
    }

    public void setDuration_cycling(double duration_cycling) {
        this.duration_cycling = duration_cycling;
    }

    public double getCalorie_walking() {
        return calorie_walking;
    }

    public void setCalorie_walking(double calorie_walking) {
        this.calorie_walking = calorie_walking;
    }

    public double getCalorie_running() {
        return calorie_running;
    }

    public void setCalorie_running(double calorie_running) {
        this.calorie_running = calorie_running;
    }

    public double getCalorie_cycling() {
        return calorie_cycling;
    }

    public void setCalorie_cycling(double calorie_cycling) {
        this.calorie_cycling = calorie_cycling;
    }
}
