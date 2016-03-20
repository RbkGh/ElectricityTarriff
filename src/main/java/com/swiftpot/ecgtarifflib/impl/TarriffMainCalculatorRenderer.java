package com.swiftpot.ecgtarifflib.impl;

import com.swiftpot.ecgtarifflib.model.ApplianceItem;
import com.swiftpot.ecgtarifflib.model.TarriffCalculationRequestPayload;
import com.swiftpot.ecgtarifflib.model.TarriffCalculationResponePayload;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at rbk.unlimited@gmail.com> on
 *         17-Mar-16
 */
public class TarriffMainCalculatorRenderer /*implements TarriffMainCalculator*/{

    private final static double HOURS_IN_MONTH = 720;
    private final static double HIGHEST_DIFFERENCE = 5001;
    private final static String UNITS_KW_ROW_NAME = "Units (kWh)";

    private int FINAL_TOTAL_UNITS_IN_WATTS  = 0;
    private String FINAL_GOVT_SUBSIDY_AMOUNT = "0";
    private String FINAL_TOTAL_COST_AMOUNT = "0";
    private static String CURRENCY = "GHS";



    TarriffCalculationRequestPayload tarriffCalculationRequestPayload ;
    TarriffCalculationResponePayload tarriffCalculationResponePayload ;

    public TarriffMainCalculatorRenderer(TarriffCalculationRequestPayload tarriffCalculationRequestPayload) {
        this.tarriffCalculationRequestPayload = tarriffCalculationRequestPayload;

    }
    public void calculateTarriff(){
        List<Double> totalHoursForMonthForEachApplianceItemList = new ArrayList<Double>(0);
        List<Double> totalUnitsForEachApplianceItemList = new ArrayList<Double>(0);
        for (ApplianceItem applianceItem : tarriffCalculationRequestPayload.getApplianceItemList()) {
            double totalHoursForOneApplianceItemForMonth = findTotalHoursForMonthForOneApplianceItemInList(applianceItem.getApplianceHours());
            totalHoursForMonthForEachApplianceItemList.add(totalHoursForOneApplianceItemForMonth);
            double totalUnitsForOneApplianceItemForMonth = findTotalUnitsForOneApplianceItemInList(totalHoursForOneApplianceItemForMonth,
                    applianceItem.getApplianceWatts(),
                    applianceItem.getApplianceQty());

            totalUnitsForEachApplianceItemList.add(totalUnitsForOneApplianceItemForMonth);

        }

        //set to FINAL_TOTAL_UNITS_IN_WATTS make sure its a whole number
        DecimalFormat decimalFormat=new DecimalFormat("#");
        FINAL_TOTAL_UNITS_IN_WATTS = Integer.valueOf(decimalFormat.format(findTotalUnitsForAllApplianceItemsInList(totalUnitsForEachApplianceItemList)));
        System.out.println("FINAL_TOTAL_UNITS_IN_WATTS=" + FINAL_TOTAL_UNITS_IN_WATTS);

        findTotalGovtSubsidyAndTotalCost(Double.valueOf(FINAL_TOTAL_UNITS_IN_WATTS));//we need it as 4.0,5.0,or 10.0,hence convert to double,since comparison is in that format


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
            /*C:\Users\Rodney\Downloads\*/ /*ecg_tarriff_calculation.xlsx*/
            FileInputStream file = new FileInputStream(new File("C:\\Users\\Rodney\\Downloads\\ecg_tarriff_calculation.xlsx"));

            System.out.println("FILE FOUND! totalUnitsForAllApplianceItemsInList ="+String.valueOf(totalUnitsForAllApplianceItemsInList));
            //Get the workbook instance for XLS file

            Workbook workbook = WorkbookFactory.create(file);

            boolean foundGovtSubsidyAndTotalCost = false;
            for(int i =0; i <= HIGHEST_DIFFERENCE ; i++) {
                double totalUnitsForAllApplianceItemsInListTOSEARCHNOW = totalUnitsForAllApplianceItemsInList + i;
            for(Sheet currentSheet : workbook){
                if(foundGovtSubsidyAndTotalCost){
                    return;//get out of loop since found
                }
                for(Row row : currentSheet){
                    for(Cell cell : row) {


                        switch (cell.getCellType()) {
                            case Cell.CELL_TYPE_STRING:

                                try {
                                    System.out.println("cell at 0 position is =  " + row.getCell(0).getRichStringCellValue().getString());
                                    System.out.println("cell at 8 position is  =" + row.getCell(8).getRichStringCellValue().getString());
                                    System.out.println("cell at 9 position is =" + row.getCell(9).getRichStringCellValue().getString());
                                    if (row.getCell(0).getRichStringCellValue().getString().equals(String.valueOf(totalUnitsForAllApplianceItemsInListTOSEARCHNOW))) {
                                        System.out.println("Match Found");

                                        this.FINAL_GOVT_SUBSIDY_AMOUNT = row.getCell(8).getRichStringCellValue().getString();
                                        this.FINAL_TOTAL_COST_AMOUNT = row.getCell(9).getRichStringCellValue().getString();
                                        foundGovtSubsidyAndTotalCost = true;

                                    }
                                } catch (IllegalStateException ile) {
                                    System.out.println("cell at 0 position is =  " + row.getCell(0).getNumericCellValue());
                                    System.out.println("cell at 8 position is  =" + row.getCell(8).getNumericCellValue());
                                    System.out.println("cell at 9 position is =" + row.getCell(9).getNumericCellValue());
                                    if (String.valueOf(row.getCell(0).getNumericCellValue()).equals(String.valueOf(totalUnitsForAllApplianceItemsInListTOSEARCHNOW))) {
                                        System.out.println("Match Found");
                                        this.FINAL_GOVT_SUBSIDY_AMOUNT = String.valueOf(row.getCell(8).getNumericCellValue());
                                        this.FINAL_TOTAL_COST_AMOUNT = String.valueOf(row.getCell(9).getNumericCellValue());
                                        foundGovtSubsidyAndTotalCost = true;
                                    }
                                }
                                break;
                            case Cell.CELL_TYPE_NUMERIC:
                                if (DateUtil.isCellDateFormatted(cell)) {
                                    System.out.println(cell.getDateCellValue());
                                } else {
                                    System.out.println("cell at 0 position is =  " + row.getCell(0).getNumericCellValue());
                                    System.out.println("cell at 8 position is  =" + row.getCell(8).getNumericCellValue());
                                    System.out.println("cell at 9 position is =" + row.getCell(9).getNumericCellValue());
                                    System.out.println(cell.getNumericCellValue());
                                    if (String.valueOf(row.getCell(0).getNumericCellValue()).equals(String.valueOf(totalUnitsForAllApplianceItemsInListTOSEARCHNOW))) {
                                        System.out.println("Match Found");
                                        this.FINAL_GOVT_SUBSIDY_AMOUNT = String.valueOf(row.getCell(8).getNumericCellValue());
                                        this.FINAL_TOTAL_COST_AMOUNT = String.valueOf(row.getCell(9).getNumericCellValue());
                                        foundGovtSubsidyAndTotalCost = true;
                                    }
                                }
                                break;
                            case Cell.CELL_TYPE_BOOLEAN:

                                System.out.println("Boolean Value = " + cell.getBooleanCellValue());
                                break;
                            case Cell.CELL_TYPE_FORMULA:
                                System.out.println("Cell Type Formula = " + cell.getCellFormula());
                                break;
                            default:
                                System.out.println("General String");
                                System.out.println("cell at 0 position is =  " + row.getCell(0).getStringCellValue());
                                System.out.println("cell at 8 position is  =" + row.getCell(8).getStringCellValue());
                                System.out.println("cell at 9 position is =" + row.getCell(9).getStringCellValue());
                                if (row.getCell(0).getStringCellValue().equals(String.valueOf(totalUnitsForAllApplianceItemsInListTOSEARCHNOW))) {
                                    System.out.println("Match Found");
                                    this.FINAL_GOVT_SUBSIDY_AMOUNT = row.getCell(8).getStringCellValue();
                                    this.FINAL_TOTAL_COST_AMOUNT = row.getCell(9).getStringCellValue();
                                    foundGovtSubsidyAndTotalCost = true;
                                }
                        }

                    }

                    }
                }//close HIGHEST_DIFFERENCE for Loop
            }


            file.close();
        }

        catch(FileNotFoundException fnFE){
                //file not found.
            System.out.println("Excel File not found");
        }catch(IOException ioE){
            System.out.println("Excel File not found");
        }catch(InvalidFormatException ifE){
            System.out.println("Excel File not found");
        }

    }

    public TarriffCalculationRequestPayload getTarriffCalculationRequestPayload() {
        return tarriffCalculationRequestPayload;
    }

    public void setTarriffCalculationRequestPayload(TarriffCalculationRequestPayload tarriffCalculationRequestPayload) {
        this.tarriffCalculationRequestPayload = tarriffCalculationRequestPayload;
    }




    public double getTotalUnits() {
        return FINAL_TOTAL_UNITS_IN_WATTS;
    }


    public String getGovtSubsidyAmount() {
        return FINAL_GOVT_SUBSIDY_AMOUNT;
    }


    public String getTotalCostDue() {
        return FINAL_TOTAL_COST_AMOUNT;
    }


    public String getCurrency() {
        return CURRENCY;
    }


    public TarriffCalculationResponePayload getFullResponsePayload() {
        return null;
    }
}
