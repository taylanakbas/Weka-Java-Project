package com.taylanakbas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Score {

    public double score;
    public String version;
    public HashMap<String, Result> trainResults;
    public HashMap<String, Result> testResults;
    public List<String> variations = new ArrayList<>();
    private boolean isTrain;

    public Score(HashMap<String, Result> trainResults, HashMap<String, Result> testResults) {
        this.trainResults = trainResults;
        this.testResults = testResults;
        this.version = "";
        this.isTrain = true;
    }
    public Score(){ }
    public void calculateTableScore() {

        ArrayList<Double> tpTrain = new ArrayList<>();
        ArrayList<Double> tpTest = new ArrayList<>();

        ArrayList<String> versions = new ArrayList<>();
        Result r = new Result();
        double best = 0.0;

        for (String key : this.trainResults.keySet()) {
            versions.add(key);
        }
        for (String v : versions) {
            tpTrain.add(this.trainResults.get(v).avgTP);
            if (this.trainResults.get(v).avgTP >= best){
                this.version = v;
                best = this.trainResults.get(v).avgTP;
            }
        }
        double avgTrain = r.calculateAVG(tpTrain);
        this.score = avgTrain;

        double std = r.calculateSTD(tpTrain,avgTrain);
        if (std < 0.03) {
            for (String v : versions) {
                tpTest.add(this.testResults.get(v).avgTP);
                if (this.testResults.get(v).avgTP >= best){
                    if (this.version != v) {
                        double dif =  this.trainResults.get(v).avgTP - this.testResults.get(v).avgTP;
                        if (dif <= 0.0) {
                            this.version = v;
                            this.isTrain = false;
                        }
                    }
                    best = this.trainResults.get(v).avgTP;
                }
            }
            double avgTest = r.calculateAVG(tpTest);
            this.score = (avgTrain * 0.5) * (avgTest * 0.5);
        }
    }
    public void calculateUserScore(String algo, String t, int userSize, int variationSize) throws Exception {
        HashMap<String,Double> scores;
        String path = "";
        if (isTrain){
            path = "C:/Users//EXCEL/" + algo + t + ".xls";
        }else{
            path = "C:/Users/EXCEL/" + algo + t + "t.xls";
        }
        Converter c = new Converter(path);
        scores = c.readXls(this.version,userSize,variationSize);
        String s = "";
        double best = 0.0;
        for (String user: scores.keySet()) {
            if (user.substring(0,2) != s){
                s = user.substring(0,2);
                if (scores.get(user).doubleValue() > best){
                    best = scores.get(user).doubleValue();
                }
            }
            this.variations.add(user);
        }
    }
    public String calculateAlgorithmScore(Map<String, Score> scoreMap, List<String> algorithms, List<String> tables){
       double max = 0.0;
       String bestAlgorithm = "";
        HashMap<String,Double> algo = new HashMap<>();
        ArrayList<Double> avg = new ArrayList<>();
        Result r = new Result();
        for (String a:algorithms) {
            for (String t: tables) {
                avg.add(scoreMap.get(a + t).score);
                double algoAvg = r.calculateAVG(avg);
                algo.put(a,algoAvg);
            }
        }
        for (String a: algorithms) {
            if (algo.get(a) < 0.99){
                if(algo.get(a) >= max){
                    max = algo.get(a);
                    bestAlgorithm = a;
                }
            }
        }
        return bestAlgorithm;
    }
    public HashMap<String, String> calculateBestAlgorithmTable(Map<String, Score> scoreMap, List<String> algorithms, List<String> tables){
        double max = 0.0;
        String bestAlgo = "";
        HashMap<String,String> best = new HashMap<>();
        for (String t: tables) {
            for (String a:algorithms) {
                if (scoreMap.get(a + t).score < 0.99){
                    if (scoreMap.get(a + t).score >= max){
                        max = scoreMap.get(a + t).score;
                        bestAlgo = a;
                    }
                }
                best.put(t,bestAlgo);
            }
            max = 0;
        }
        return best;
    }
}