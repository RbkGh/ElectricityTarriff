package com.swiftpot.ecgtarifflib.test;

import com.swiftpot.ecgtarifflib.impl.TarriffMainCalculatorRenderer;
import com.swiftpot.ecgtarifflib.model.ApplianceItem;
import com.swiftpot.ecgtarifflib.model.TarriffCalculationRequestPayload;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at rbk.unlimited@gmail.com> on
 *         18-Mar-16
 */
public class TestCalculation {

    public static void main(String args[]){


        ApplianceItem applianceItem = new ApplianceItem();
        applianceItem.setApplianceQty(93);
        applianceItem.setApplianceHours(24);
        applianceItem.setApplianceWatts(10);

        List<ApplianceItem> applianceItemList = new ArrayList<ApplianceItem>(0);
        applianceItemList.add(applianceItem);

        TarriffCalculationRequestPayload tarrifCalRequest = new TarriffCalculationRequestPayload();
        tarrifCalRequest.setApplianceItemList(applianceItemList);

        TarriffMainCalculatorRenderer calculatorRenderer = new TarriffMainCalculatorRenderer(tarrifCalRequest);
        calculatorRenderer.calculateTarriff();


        System.out.println("Total cost = "+calculatorRenderer.getTotalCostDue());
        System.out.println("Total Govt Subsidy = "+calculatorRenderer.getGovtSubsidyAmount());
        System.out.println("Total Units  = "+calculatorRenderer.getTotalUnits());
        System.out.println("Currency ="+calculatorRenderer.getCurrency());

//        Double number1 = 2.00;
//        //Float rounded = new Float( Math.round(number1));
//        //Double roundedVal = Double.valueOf(String.valueOf(rounded));
//        DecimalFormat decimalFormat=new DecimalFormat("#");
//        String ll = decimalFormat.format(number1);
//
//        int finalRound = Integer.valueOf(ll);


        //System.out.println("rounded value = "+finalRound);
    }
}
