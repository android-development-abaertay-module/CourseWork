package com.example.coursework.model.helper;

//GoalCheckDTO is simple holder class used to store information about goal status
public class GoalCheckDTO {
    private String output;
    private Boolean isAchieved;

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public Boolean getIsAchieved() {
        return isAchieved;
    }

    public void setIsAchieved(Boolean achieved) {
        isAchieved = achieved;
    }

    public GoalCheckDTO(String output, Boolean isAchieved) {
        this.output = output;
        this.isAchieved = isAchieved;
    }

    public GoalCheckDTO() {
    }
}
