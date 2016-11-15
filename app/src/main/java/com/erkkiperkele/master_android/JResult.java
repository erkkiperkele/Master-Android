package com.erkkiperkele.master_android;

@SuppressWarnings("WeakerAccess")
public class JResult {

    private final float result;
    private final double executionTimeInS;

    private Long executionDateTime;
    private String algorithmName;
    private int threadsCount;
    private int taskSize;
    private String taskSizeUnit;
    private String executionDateTimePretty;

    public JResult(float result, double executionTimeInS) {
        this.result = result;

        this.executionTimeInS = executionTimeInS;
    }

    // Not ideal but that's ok
    public Long getId() {
        return executionDateTime;
    }

    public float getResult() {

        return result;
    }

    public int getTaskSize() {
        return taskSize;
    }

    public String getTaskSizeUnit() {
        return taskSizeUnit;
    }

    public String getExecutionDateTimePretty() {
        return executionDateTimePretty;
    }

    public Long getExecutionDateTime(){
        return executionDateTime;
    }

    public double getExecutionTimeInS() {
        return executionTimeInS;
    }

    public String getAlgorithmName() {
        return algorithmName;
    }

    public int getThreadsCount() {
        return threadsCount;
    }

    public JResult setId(long id) {
        this.executionDateTime = id;

        return this;
    }

    public JResult setAlgorithmName(String algorithmName) {
        this.algorithmName = algorithmName;

        return this;
    }

    @SuppressWarnings("SameParameterValue")
    public JResult setThreadsCount(int threadsCount) {
        this.threadsCount = threadsCount;

        return this;
    }

    public JResult setTaskSize(int taskSize) {
        this.taskSize = taskSize;

        return this;
    }

    public JResult setTaskSizeUnit(String taskSizeUnit) {
        this.taskSizeUnit = taskSizeUnit;

        return this;
    }

    public JResult setExecutionDateTimePretty(String executionDateTimePretty) {
        this.executionDateTimePretty = executionDateTimePretty;

        return this;
    }
}