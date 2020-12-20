package kr.co.company.myapplication;

import java.io.Serializable;
import java.util.Date;

public class Person implements Serializable {
    int code;
    private String name;
    private Date targetDate;
    private double targetLocationLatitude;
    private double targetLocationLongtitude;
    private int absence;
    // boolean isTodaySuccess;
    Date lastCheck;

    public Date getLastCheck() {
        return lastCheck;
    }

    public void setLastCheck(Date lastCheck) {
        this.lastCheck = lastCheck;
    }

    public Person() {
        code = 1000;//일단 하나만 만들거기 때문
        name = "ham";

        Date targetd = new Date();
        /*
        targetd.setHours(14);
        targetd.setMinutes(0);
        targetd.setSeconds(0);
        *///일단은 앱 생긴 즉시로 해둠
        targetLocationLatitude = 37.483346;
        targetLocationLongtitude = 126.756485;
        //isTodaySuccess=false;
        targetDate = targetd;
        absence = 0;
        lastCheck = new Date();
        lastCheck.setTime(lastCheck.getTime() - 1 * 12 * 60 * 60);
    }

    public int getAbsence() {
        return absence;
    }

    public void setAbsence(int anbscence) {
        this.absence = anbscence;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getTargetLocationLatitude() {
        return targetLocationLatitude;
    }

    public void setTargetLocationLatitude(double targetLocationLatitude) {
        this.targetLocationLatitude = targetLocationLatitude;
    }

    public double getTargetLocationLongtitude() {
        return targetLocationLongtitude;
    }

    public void setTargetLocationLongtitude(double targetLocationLongtitude) {
        this.targetLocationLongtitude = targetLocationLongtitude;
    }

    public Date getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(Date targetDate) {
        this.targetDate = targetDate;
    }

    public boolean canTodayCheck() {
        if (new Date().getTime() - targetDate.getTime() > 1 * 60 * 1000 || new Date().getTime() - targetDate.getTime() < 0) {//2분 경과
            return false;
        } else if (lastCheck.getTime() - targetDate.getTime() < 1 * 60 * 1000
                && lastCheck.getTime() - targetDate.getTime() > 0) {//이미 출석 체크함
            return false;
        } else {
            return true;
        }
    }
}