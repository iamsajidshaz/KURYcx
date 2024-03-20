package com.shaztech.kurycx.models;

public class CustomerPayments {

    private String cxName, cxPhone, cxGuid, cxPaidWeek, cxPaidOn, cxPaidAmount;

    public CustomerPayments() {
    }

    public CustomerPayments(String cxName, String cxPhone, String cxGuid, String cxPaidWeek, String cxPaidOn, String cxPaidAmount) {
        this.cxName = cxName;
        this.cxPhone = cxPhone;
        this.cxGuid = cxGuid;
        this.cxPaidWeek = cxPaidWeek;
        this.cxPaidOn = cxPaidOn;
        this.cxPaidAmount = cxPaidAmount;
    }

    public String getCxName() {
        return cxName;
    }

    public void setCxName(String cxName) {
        this.cxName = cxName;
    }

    public String getCxPhone() {
        return cxPhone;
    }

    public void setCxPhone(String cxPhone) {
        this.cxPhone = cxPhone;
    }

    public String getCxGuid() {
        return cxGuid;
    }

    public void setCxGuid(String cxGuid) {
        this.cxGuid = cxGuid;
    }

    public String getCxPaidWeek() {
        return cxPaidWeek;
    }

    public void setCxPaidWeek(String cxPaidWeek) {
        this.cxPaidWeek = cxPaidWeek;
    }

    public String getCxPaidOn() {
        return cxPaidOn;
    }

    public void setCxPaidOn(String cxPaidOn) {
        this.cxPaidOn = cxPaidOn;
    }

    public String getCxPaidAmount() {
        return cxPaidAmount;
    }

    public void setCxPaidAmount(String cxPaidAmount) {
        this.cxPaidAmount = cxPaidAmount;
    }
}
