package com.finix.kisiiattend;

public class TotalAttandance {
    String regNo,sName,timeAttended;

    public TotalAttandance() {
    }

    public TotalAttandance(String regNo, String sName, String timeAttended) {
        this.regNo = regNo;
        this.sName = sName;
        this.timeAttended = timeAttended;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public String getTimeAttended() {
        return timeAttended;
    }

    public void setTimeAttended(String timeAttended) {
        this.timeAttended = timeAttended;
    }
}
