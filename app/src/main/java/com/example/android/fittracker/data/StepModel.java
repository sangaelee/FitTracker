package com.example.android.fittracker.data;

public class StepModel {
    public int Step;
    public int Stepsize;
    public int Unit;          //1-Km, 2-Miles
    public double BodyWeight;
    public int ExeciseType;  //1-Walking, 2-Running
    public int Sensitivity;  //     1.97  4.44  10, 22.5 50.62

    public StepModel() {
    }

    public int getStep() {
        return Step;
    }

    public void setStep(int step) {
        Step = step;
    }

    public int getStepsize() {
        return Stepsize;
    }

    public void setStepsize(int stepsize) {
        Stepsize = stepsize;
    }

    public int getUnit() {
        return Unit;
    }

    public void setUnit(int unit) {
        Unit = unit;
    }

    public double getBodyWeight() {
        return BodyWeight;
    }

    public void setBodyWeight(double bodyWeight) {
        BodyWeight = bodyWeight;
    }

    public int getExeciseType() {
        return ExeciseType;
    }

    public void setExeciseType(int execiseType) {
        ExeciseType = execiseType;
    }

    public int getSensitivity() {
        return Sensitivity;
    }

    public void setSensitivity(int sensitivity) {
        Sensitivity = sensitivity;
    }
}
