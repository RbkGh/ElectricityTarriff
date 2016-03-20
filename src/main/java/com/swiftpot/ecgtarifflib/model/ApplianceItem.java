package com.swiftpot.ecgtarifflib.model;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at rbk.unlimited@gmail.com> on
 *         18-Mar-16
 */
public class ApplianceItem{

    private String applianceName;

    private String applianceCode;

    private double applianceWatts;

    private int applianceQty;

    private int applianceHours;

    public String getApplianceName() {
        return applianceName;
    }

    public void setApplianceName(String applianceName) {
        this.applianceName = applianceName;
    }

    public String getApplianceCode() {
        return applianceCode;
    }

    public void setApplianceCode(String applianceCode) {
        this.applianceCode = applianceCode;
    }

    public double getApplianceWatts() {
        return applianceWatts;
    }

    public void setApplianceWatts(double applianceWatts) {
        this.applianceWatts = applianceWatts;
    }

    public int getApplianceQty() {
        return applianceQty;
    }

    public void setApplianceQty(int applianceQty) {
        this.applianceQty = applianceQty;
    }

    public int getApplianceHours() {
        return applianceHours;
    }

    public void setApplianceHours(int applianceHours) {
        this.applianceHours = applianceHours;
    }
}