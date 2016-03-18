package com.swiftpot.ecgtarifflib.impl;

import com.swiftpot.ecgtarifflib.interfaces.TarriffMainCalculator;
import com.swiftpot.ecgtarifflib.model.TarriffCalculationRequestPayload;
import com.swiftpot.ecgtarifflib.model.TarriffCalculationResponePayload;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at rbk.unlimited@gmail.com> on
 *         17-Mar-16
 */
public class TarriffMainCalculatorRenderer implements TarriffMainCalculator{

    private final static double HOURS_IN_MONTH = 720;
    private final static double HIGHEST_DIFFERENCE = 5001;

    private static double FINAL_TOTAL_UNITS_IN_WATTS  = 0;
    private static double FINAL_GOVT_SUBSIDY_AMOUNT = 0;
    private static double FINAL_TOTAL_COST_AMOUNT = 0;
    private static String CURRENCY = "GHS";

    TarriffCalculationRequestPayload tarriffCalculationRequestPayload ;
    TarriffCalculationResponePayload tarriffCalculationResponePayload ;

    public TarriffMainCalculatorRenderer(TarriffCalculationRequestPayload tarriffCalculationRequestPayload) {
        this.tarriffCalculationRequestPayload = tarriffCalculationRequestPayload;
        calculateTarriff();
    }
    void calculateTarriff(){
        List<Double> totalHoursForMonthForEachApplianceItemList = new ArrayList<Double>(0);
        List<Double> totalUnitsForEachApplianceItemList = new ArrayList<Double>(0);
        for (TarriffCalculationRequestPayload.ApplianceItem applianceItem : tarriffCalculationRequestPayload.getApplianceItemList()) {
            double totalHoursForOneApplianceItemForMonth = findTotalHoursForMonthForOneApplianceItemInList(applianceItem.getApplianceHours());
            totalHoursForMonthForEachApplianceItemList.add(totalHoursForOneApplianceItemForMonth);
            double totalUnitsForOneApplianceItemForMonth = findTotalUnitsForOneApplianceItemInList(totalHoursForOneApplianceItemForMonth,
                                                                                                   applianceItem.getApplianceWatts(),
                                                                                                   applianceItem.getApplianceQty());
            totalUnitsForEachApplianceItemList.add(totalUnitsForOneApplianceItemForMonth);

        }

        //set to FINAL_TOTAL_UNITS_IN_WATTS
        FINAL_TOTAL_UNITS_IN_WATTS = findTotalUnitsForAllApplianceItemsInList(totalUnitsForEachApplianceItemList);

        findTotalGovtSubsidyAndTotalCost(FINAL_TOTAL_UNITS_IN_WATTS);

    }
    double findTotalHoursForMonthForOneApplianceItemInList(int applianceHours){

        double totalHoursForMonthForAppliance = HOURS_IN_MONTH / applianceHours;

        return totalHoursForMonthForAppliance;
    }



    double findTotalUnitsForOneApplianceItemInList(double totalHoursForMonthForApplianceItem , double applianceWatts,double applianceQty){

        double totalUnitsForOneQty = (totalHoursForMonthForApplianceItem / applianceWatts) * (applianceQty) ;

        return totalUnitsForOneQty;
    }

    double findTotalUnitsForAllApplianceItemsInList(List<Double> finalTotalUnitsForEachApplianceItemList){

        double sumOfAllUnits = 0;
        for(Double totalUnitsForApplianceItem : finalTotalUnitsForEachApplianceItemList) {
            sumOfAllUnits += totalUnitsForApplianceItem;
        }

        return sumOfAllUnits;
    }

    void findTotalGovtSubsidyAndTotalCost(double totalUnitsForAllApplianceItemsInList) {
        try {
            FileInputStream file = new FileInputStream(new File("ecg_tarriff_calculation.xlsx"));

            //Get the workbook instance for XLS file
            HSSFWorkbook workbook = new HSSFWorkbook(file);
            //Get first sheet from the workbook
            HSSFSheet sheet = workbook.getSheetAt(0);

            //Iterate through each rows from first sheet
            Iterator<Row> rowIterator = sheet.iterator();

            for(int i =0; i <= HIGHEST_DIFFERENCE ; i++) {
                while (rowIterator.hasNext()) {
                    Row currentRow = rowIterator.next();
                    if (currentRow.getCell(0).getStringCellValue() == String.valueOf(totalUnitsForAllApplianceItemsInList + i)) {
                        //subsidy cell is number 7
                        String govtSubsidyString = currentRow.getCell(7).getStringCellValue();
                        FINAL_GOVT_SUBSIDY_AMOUNT = Double.valueOf(govtSubsidyString);
                        String totalCostAmount = currentRow.getCell(8).getStringCellValue();
                        FINAL_TOTAL_COST_AMOUNT = Double.valueOf(totalCostAmount);
                        //found,thus exit loop
                        return ;
                    } else {
                        //not found
                        System.out.println("Couldn't find "+totalUnitsForAllApplianceItemsInList+i+"....will search for next by iterating ");
                    }
                }
            }
        }catch(FileNotFoundException fnFE){
                //file not found.
            System.out.println("Excel File not found");
        }catch(IOException ioE){
            System.out.println("Excel File not found");
        }

    }

    public TarriffCalculationRequestPayload getTarriffCalculationRequestPayload() {
        return tarriffCalculationRequestPayload;
    }

    public void setTarriffCalculationRequestPayload(TarriffCalculationRequestPayload tarriffCalculationRequestPayload) {
        this.tarriffCalculationRequestPayload = tarriffCalculationRequestPayload;
    }



    @Override
    public double getTotalUnits() {
        return FINAL_TOTAL_UNITS_IN_WATTS;
    }

    @Override
    public double getGovtSubsidyAmount() {
        return FINAL_GOVT_SUBSIDY_AMOUNT;
    }

    @Override
    public double getTotalCostDue() {
        return FINAL_TOTAL_COST_AMOUNT;
    }

    @Override
    public String getCurrency() {
        return CURRENCY;
    }

    @Override
    public TarriffCalculationResponePayload getFullResponsePayload() {
        return null;
    }
}
