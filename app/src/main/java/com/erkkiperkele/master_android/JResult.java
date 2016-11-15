package com.erkkiperkele.master_android;

public class JResult {
    private float result;
    private double executionTimeInS;

    public JResult(float result, double executionTimeInS) {
        this.result = result;
        this.executionTimeInS = executionTimeInS;
    }

    public float getResult() {
        return result;
    }

    public double getExecutionTimeInS() {
        return executionTimeInS;
    }
}