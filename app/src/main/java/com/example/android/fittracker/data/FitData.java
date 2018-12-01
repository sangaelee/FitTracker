package com.example.android.fittracker.data;

public class FitData {
    private String fitdate;
    private String distance;
    private String duration;
    private String calorie;
    private String step;

    public FitData() {
    }

    public String getFitdate() {
        return fitdate;
    }

    public void setFitdate(String fitdate) {
        this.fitdate = fitdate;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getCalorie() {
        return calorie;
    }

    public void setCalorie(String calorie) {
        this.calorie = calorie;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }
}
