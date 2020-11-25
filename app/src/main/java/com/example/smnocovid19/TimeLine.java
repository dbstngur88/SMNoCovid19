package com.example.smnocovid19;

import java.util.Date;

public class TimeLine {
    //타임라인 클래스
    String buildingName; //건물 명
    String buildingFloor;// 건물 층
    String updateDate;// 업데이트 날짜

    public TimeLine() {

    }
    public TimeLine(String buildingName,String buildingFloor,String updateDate) {
        this.buildingName = buildingName;
        this.buildingFloor = buildingFloor;
        this.updateDate = updateDate;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getBuildingFloor() {
        return buildingFloor;
    }

    public void setBuildingFloor(String buildingFloor) {
        this.buildingFloor = buildingFloor;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }
}
