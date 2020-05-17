package com.taylanakbas;

import jxl.Workbook;
import jxl.format.Colour;
import jxl.write.*;
import jxl.write.Number;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

public class Classify {
    private   List<String> users;
    private  Classifier classifier;
    private  String table;
    private String pathName;
    private HashMap<Integer,String> versions = new HashMap<>();
    private int totalVariation;
    private ArrayList<Result> results = new ArrayList<>();
    private boolean isTrain;

    public Classify(List<String> users, Classifier classifier, String table, String pathName, boolean isTrain){
        this.users = users;
        this.classifier = classifier;
        this.table = table;
        this.pathName = pathName;
        this.isTrain = isTrain;
        this.totalVariation = 10;
        this.init();
    }
    private void init() {
        this.versions.put(0, "");
        this.versions.put(1, "A");
        this.versions.put(2, "B");
        this.versions.put(3, "C");
        this.versions.put(4, "D");
        if(table == "call") versions.put(5, "E");
        File cls = new File("/Users/taylanakbas/Desktop/EXCEL_FILES/"+ pathName);
        cls.mkdirs(); // creates new folder - mkdir() for Windows
    }
    public void evaluate() throws Exception{
        BufferedReader reader;
        Instances traindata;
        WritableWorkbook workbook = null;
        WritableSheet sheet = null;

        WritableFont arial10font = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD, false);
        arial10font.setColour(Colour.RED);
        WritableCellFormat arial10format_USER = new WritableCellFormat(arial10font);

        arial10format_USER.setWrap(true);
        WritableFont arial10font_3 = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD, false);
        WritableCellFormat arial10format_3 = new WritableCellFormat(arial10font_3);

        arial10format_3.setShrinkToFit(true);
        WritableFont arial10font_DETAIL = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD, false);

        arial10font_DETAIL.setColour(Colour.BLUE);
        WritableFont arial10format_DETAIL = new WritableFont(arial10font_DETAIL);

        NumberFormat fourdps = new NumberFormat("0.####");
        WritableCellFormat fourdpsFormat = new WritableCellFormat(arial10format_DETAIL, fourdps);
        fourdpsFormat.setWrap(true);

        int count1; int count2;
        String excelFilePath;

        if (isTrain){  excelFilePath = "/Users/EXCEL_FILES/"+ this.pathName + "/" + this.pathName + this.table + ".xls"; } //TRAIN
        else {  excelFilePath = "/Users/EXCEL_FILES/"+ this.pathName + "/" + this.pathName + this.table + "t.xls"; } // TEST
        workbook = Workbook.createWorkbook(new File(excelFilePath));

        for (int k = 0; k < versions.size(); k++) {

            count1 = 0; count2 = 0;

            if(versions.get(k) == "" ) { sheet = workbook.createSheet("ALL", k); }
            else { sheet = workbook.createSheet(versions.get(k), k);}

            Label label = new Label(totalVariation + 6, count2, "Avg", arial10format_USER);
            sheet.addCell(label);
            label = new Label(totalVariation + 7, count2, "Std", arial10format_USER);
            sheet.addCell(label);
            label = new Label(totalVariation + 5, count2 + 1, "TA", arial10format_USER);
            sheet.addCell(label);
            label = new Label(totalVariation + 5, count2 + 2, "TP", arial10format_USER);
            sheet.addCell(label);
            label = new Label(totalVariation + 5, count2 + 3, "TN", arial10format_USER);
            sheet.addCell(label);

            for (int i = 0; i < users.size(); i++) {

                label = new Label(0, count1, this.users.get(i), arial10format_USER);
                sheet.addCell(label);
                label = new Label(0,   count1 + 1, "TA", arial10format_3);
                sheet.addCell(label);
                label = new Label(0,   count1 + 2, "TP", arial10format_3);
                sheet.addCell(label);
                label = new Label(0,   count1 + 3, "TN", arial10format_3);
                sheet.addCell(label);
                label = new Label(totalVariation + 2, count2, "Avg", arial10format_USER);
                sheet.addCell(label);
                label = new Label(totalVariation + 3, count2, "Std", arial10format_USER);
                sheet.addCell(label);

                String variation;
                ArrayList<Double> TA =  new ArrayList<>();
                ArrayList<Double> TP =  new ArrayList<>();
                ArrayList<Double> TN =  new ArrayList<>();

                for (int j = 1; j <= this.totalVariation; j++) {

                    if(j < 10){ variation = "0" + j; }
                    else { variation = Integer.toString(j); }
                    String dataPath = "/Users/ARFF_FILES/" + table + this.users.get(i) + variation + versions.get(k).toLowerCase() + ".arff";
                    reader = new BufferedReader(new FileReader(dataPath));
                    traindata = new Instances(reader);
                    reader.close();

                    traindata.setClassIndex(traindata.numAttributes() - 1); // setting class attribute
                    Classifier cls = this.classifier;
                    cls.buildClassifier(traindata);
                    Evaluation eval = new Evaluation(traindata);

                    if (isTrain){
                        eval.evaluateModel(cls, traindata);
                    }else {
                        String testPath = "/Users/ARFF_FILES/" + table + "t" + this.users.get(i) + "01" + versions.get(k).toLowerCase() +  ".arff";
                        reader = new BufferedReader(new FileReader(testPath));
                        Instances testdata = new Instances(reader);
                        reader.close();
                        testdata.setClassIndex(testdata.numAttributes()-1);
                        eval.evaluateModel(cls, testdata);
                    }

                    label = new Label(j, count2, users.get(i) + variation, arial10format_USER);
                    sheet.addCell(label);

                    Number number = new Number(j, count2 + 1, eval.pctCorrect() / 100, fourdpsFormat); //Correctly classified instance
                    sheet.addCell(number);
                    TA.add(eval.pctCorrect() / 100);

                    number = new Number(j, count2 + 2, eval.truePositiveRate(0), fourdpsFormat); //TruePositive
                    sheet.addCell(number);
                    TP.add(eval.truePositiveRate(0));

                    number = new Number(j, count2 + 3, eval.trueNegativeRate(0), fourdpsFormat); //TrueNegative
                    sheet.addCell(number);
                    TN.add(eval.trueNegativeRate(0));

                    if (isTrain)
                        System.out.println("CHECK TO RUN " + this.table  + users.get(i) + variation + versions.get(k) + " TRAIN for " + pathName);
                    else
                        System.out.println("CHECK TO RUN " + this.table  + users.get(i) + variation + versions.get(k) + " TEST for " + pathName);
                }
                Result result =  new Result(TA,TP,TN);
                result.calculateResults();
                results.add(result);

                Number avgTACell = new Number(totalVariation + 2, count2 + 1, result.avgTA, fourdpsFormat);
                Number avgTPCell = new Number(totalVariation + 2, count2 + 2, result.avgTP, fourdpsFormat);
                Number avgTNCell = new Number(totalVariation + 2, count2 + 3, result.avgTN, fourdpsFormat);
                Number stdTACell = new Number(totalVariation + 3, count2 + 1, result.stdTA, fourdpsFormat);
                Number stdTPCell = new Number(totalVariation + 3, count2 + 2, result.stdTP, fourdpsFormat);
                Number stdTNCell = new Number(totalVariation + 3, count2 + 3, result.stdTN, fourdpsFormat);
                sheet.addCell(avgTACell);
                sheet.addCell(avgTPCell);
                sheet.addCell(avgTNCell);
                sheet.addCell(stdTACell);
                sheet.addCell(stdTPCell);
                sheet.addCell(stdTNCell);

                count2 = count2 +  4;
                count1 = count1 + 4;
            }
            count2 = 0;
            Result totalResult =  new Result();
            totalResult.calculateTotalResults(results);

            Number number = new Number(totalVariation + 6, count2 + 1, totalResult.avgTA, fourdpsFormat);
            sheet.addCell(number);
            number = new Number(totalVariation + 6,  count2 + 2, totalResult.avgTP, fourdpsFormat);
            sheet.addCell(number);
            number = new Number(totalVariation + 6,  count2 + 3, totalResult.avgTN, fourdpsFormat);
            sheet.addCell(number);
            number = new Number(totalVariation + 7,  count2 + 1, totalResult.stdTA, fourdpsFormat);
            sheet.addCell(number);
            number = new Number(totalVariation + 7,  count2 + 2, totalResult.stdTP, fourdpsFormat);
            sheet.addCell(number);
            number = new Number(totalVariation + 7,  count2 + 3, totalResult.stdTN, fourdpsFormat);
            sheet.addCell(number);
        }
        workbook.write();
        workbook.close();
    }
}