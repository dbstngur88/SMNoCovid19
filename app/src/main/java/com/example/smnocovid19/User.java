package com.example.smnocovid19;

public class User {
    public String userNumber;
    public String buildingFloor;
    public String buildingName;
    public String updateTime;

    public User() {

    }

    public User(String buildingFloor, String buildingName, String updateTime) {
        this.buildingFloor = buildingFloor;
        this.buildingName = buildingName;
        this.updateTime = updateTime;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public String getBuildingFloor() {
        return buildingFloor;
    }

    public void setBuildingFloor(String buildingFloor) {
        this.buildingFloor = buildingFloor;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "timeline{" +
                " userNumber ='" + userNumber + '\'' +
                " buildingFloor ='" + buildingFloor + '\'' +
                ", buildingName ='" + buildingName + '\'' +
                ", updateTime ='" + updateTime + '\'' +
                '}';
    }

}
