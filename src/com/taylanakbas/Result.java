package com.taylanakbas;
import java.util.ArrayList;

public class Result {

    public ArrayList<Double> TA = new ArrayList<>();
    public ArrayList<Double> TP = new ArrayList<>();
    public ArrayList<Double> TN = new ArrayList<>();
    private ArrayList<Double> results = new ArrayList<>();
    public double avgTA; public double avgTP; public double avgTN;
    public double stdTA; public double stdTP; public double stdTN;

    public Result(){ }
    public Result(ArrayList<Double> TA, ArrayList<Double> TP, ArrayList<Double> TN){
        this.TA = TA;
        this.TP = TP;
        this.TN = TN;
    }


    public void calculateResults(){
        this.avgTA = this.calculateAVG(TA);
        this.avgTP = this.calculateAVG(TP);
        this.avgTN = this.calculateAVG(TN);
        this.stdTA = this.calculateSTD(TA, avgTA);
        this.stdTP = this.calculateSTD(TP, avgTP);
        this.stdTN = this.calculateSTD(TN, avgTN);
        results.add(avgTA);
        results.add(avgTP);
        results.add(avgTN);
        results.add(stdTA);
        results.add(stdTP);
        results.add(stdTN);
    }
    public void calculateTotalResults(ArrayList<Result> results){
        for (Result res: results) {
            this.TA.add(res.avgTA);
            this.TP.add(res.avgTP);
            this.TN.add(res.avgTN);
        }
        calculateResults();
    }
    public double calculateAVG(ArrayList<Double> result){
        double total = 0;
        for (Number res:result) {
            total += res.doubleValue();
        }
        return (total / result.size());
    }
    public double calculateSTD(ArrayList<Double> result, double avg){
        double std = 0;
        for (Number res:result) {
            std += Math.pow(res.doubleValue() - avg, 2);
        }
        return Math.sqrt(std/result.size());
    }
}
