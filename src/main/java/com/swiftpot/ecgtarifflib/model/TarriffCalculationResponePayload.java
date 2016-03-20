package com.swiftpot.ecgtarifflib.model;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at rbk.unlimited@gmail.com> on
 *         17-Mar-16
 */
public class TarriffCalculationResponePayload {

    private TarriffCalculationRequestPayload tarriffRequest;

    private long totalUnits;

    private long totalGovtSubsidy;

    private long totalCostDue;

    private String currencyType;

    public TarriffCalculationRequestPayload getTarriffRequest() {
        return tarriffRequest;
    }

    public void setTarriffRequest(TarriffCalculationRequestPayload tarriffRequest) {
        this.tarriffRequest = tarriffRequest;
    }

    public long getTotalUnits() {
        return totalUnits;
    }

    public void setTotalUnits(long totalUnits) {
        this.totalUnits = totalUnits;
    }

    public long getTotalGovtSubsidy() {
        return totalGovtSubsidy;
    }

    public void setTotalGovtSubsidy(long totalGovtSubsidy) {
        this.totalGovtSubsidy = totalGovtSubsidy;
    }

    public long getTotalCostDue() {
        return totalCostDue;
    }

    public void setTotalCostDue(long totalCostDue) {
        this.totalCostDue = totalCostDue;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }
}
