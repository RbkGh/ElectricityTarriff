package com.swiftpot.ecgtarifflib.interfaces;

import com.swiftpot.ecgtarifflib.model.TarriffCalculationResponePayload;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at rbk.unlimited@gmail.com> on
 *         17-Mar-16
 */
public interface TarriffMainCalculator {

    double getTotalUnits();

    double getGovtSubsidyAmount();

    double getTotalCostDue();

    String getCurrency();

    TarriffCalculationResponePayload getFullResponsePayload();
}
