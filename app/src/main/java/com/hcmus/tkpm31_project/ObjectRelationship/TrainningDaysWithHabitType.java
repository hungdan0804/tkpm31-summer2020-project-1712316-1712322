package com.hcmus.tkpm31_project.ObjectRelationship;

public class TrainningDaysWithHabitType {
    private String habiType;
    private long totalTime;

    public TrainningDaysWithHabitType(String habiType,long totalTime){
        this.habiType =habiType;
        this.totalTime=totalTime;
    }

    public String getHabiType() {
        return habiType;
    }

    public void setHabiType(String habiType) {
        this.habiType = habiType;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }
}
