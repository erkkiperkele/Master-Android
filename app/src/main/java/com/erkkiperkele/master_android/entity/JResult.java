package com.erkkiperkele.master_android.entity;

@SuppressWarnings("WeakerAccess")
public class JResult {

    private float result;
    private double executionTimeInS;

    private Long executionDateTime;
    private String algorithmName;
    private int threadsCount;
    private int taskSize;
    private String taskSizeUnit;
    private String executionDateTimePretty;

    // FirebaseAdapter requires an empty constructor
    // Would be nice to separate entities needed for c++ / firebase write / firebase read
    public JResult() {
    }

    public JResult(float result, double executionTimeInS) {
        this.result = result;

        this.executionTimeInS = executionTimeInS;
    }

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

    @SuppressWarnings("SameParameterValue") // Will be removed once multi-thread comes in
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