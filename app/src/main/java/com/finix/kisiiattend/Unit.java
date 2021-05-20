package com.finix.kisiiattend;

public class Unit {
    String userId,unitCode,unitName,unitPassword,unitTimes;


    public Unit(String userId, String unitName) {
        this.userId = userId;
        this.unitName = unitName;
    }

    public Unit(String unitTimes, String unitCode, String unitName) {
        this.unitTimes = unitTimes;
        this.unitCode = unitCode;
        this.unitName = unitName;
    }

    public Unit(String userId, String unitCode, String unitName, String unitPassword, String unitTimes) {
        this.userId = userId;
        this.unitCode = unitCode;
        this.unitName = unitName;
        this.unitPassword = unitPassword;
        this.unitTimes = unitTimes;
    }

    public Unit() {
    }

    public String getUnitPassword() {
        return unitPassword;
    }

    public void setUnitPassword(String unitPassword) {
        this.unitPassword = unitPassword;
    }

    public String getUnitTimes() {
        return unitTimes;
    }

    public void setUnitTimes(String unitTimes) {
        this.unitTimes = unitTimes;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }
}
