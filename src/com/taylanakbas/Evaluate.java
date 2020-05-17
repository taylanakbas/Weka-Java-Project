package com.taylanakbas;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Evaluate {

    // Evaluate Steps:

    // for Table Score
    // 1 - Reads each .xls file for an algorithm (algorithm_table_train.xls + algorithm_table_test.xls)
    // 2 - Picks best version of train results according to average TP
    // 3 - Calculates the table score using by mean of train result's average TP values
    // 4 - IF standard deviation of train result's TP values  < 0.03
    //      Calculates the new table score using by mean of test result's average TP values
    //      IF new table score > old table score
    //          Update version with new one.
    //      Update score -> (avgOfTrain * 0.5) * (avgOfTest * 0.5)

    // for User Score
    // 1 - Reads each .xls file (algorithm_table_train.xls or algorithm_table_test.xls)
    // 2 - Picks best variation for each users

    // for Best Algorithm Score (each table)
    // 1 - Compare all table's scores for each algorithm
    // 2 - Pick the best

    // for Best Algorithm Score  (entire system)
    // 1 - Sum all table's scores for each algorithm
    // 2 - Calculate its average
    // 3 - Pick the best condition with => BestScore < 0.99

    public List<String> tables;
    public List<String> versions;
    public List<String> algorithms;
    public List<String> users;
    public List<String> variations;

    public Evaluate(List<String> algorithms, List<String> tables, List<String> versions,List<String> variations,List<String> users){
        this.algorithms = algorithms;
        this.tables = tables;
        this.versions = versions;
        this.variations = variations;
        this.users = users;
    }
    public void run() throws Exception {

        HashMap<String, Result> trainResults = new HashMap<>();
        HashMap<String, Result> testResults = new HashMap<>();
        Map<String, Score> tableScores = new HashMap<>();
        Converter c;
        Score best = new Score();

        for (String algo : algorithms){
            for (String t:tables) {
                for (String v:versions) {
                    if (t != "call" && v == "e") break;

                    String trainPath = "C:/Users/EXCEL/" + algo + t + ".xls";
                    String testPath = "C:/Users/EXCEL/" + algo + t + "t.xls";
                    c = new Converter(trainPath);
                    trainResults = c.readXls(trainResults,v);
                    c = new Converter(testPath);
                    testResults = c.readXls(testResults,v);
                }
                best = new Score(trainResults,testResults);
                best.calculateTableScore();
                best.calculateUserScore(algo, t, users.size(), variations.size());
                best.score = best.score * 4;
                tableScores.put((algo + t),best);
                trainResults = new HashMap<>();
                testResults = new HashMap<>();
            }
        }
        System.out.println("Best algorithm for all system: " + best.calculateAlgorithmScore(tableScores,algorithms,tables));
        System.out.println("Best algorithm for every tables:" + best.calculateBestAlgorithmTable(tableScores,algorithms,tables));
    }
}
