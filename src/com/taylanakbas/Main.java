package com.taylanakbas;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;

import java.util.Arrays;
import java.util.List;

public class Main {

    public static List < String > users = Arrays.asList("08", "15", "20", "21", "31", "35", "37", "43", "47", "66", "78", "89", "91", "92", "97");
    public static List<String> tables = Arrays.asList("call","activity","device","cs");
    public static List<String> versions = Arrays.asList("","a","b","c","d","e");
    public static List<String> variations = Arrays.asList("01","02","03","04","05","06","07","08","09","10");
    public static List<String> algorithms = Arrays.asList(
            "NaiveBayes", "BayesNet", "Logistic", "OneR", "JRip", "AdaBoostM1", "Vote", "DecisionTable",
            "J48", "IBk", "VotedPerceptron", "LogitBoost", "HoeffdingTree", "RandomTree", "RandomForest",
            "MultiScheme", "SMO", "ClassificationViaRegression", "MultilayerPerceptron", "LWL"
    );

    public static void main(String[] args) throws Exception {
        Classifier cls =  new NaiveBayes();
        String path = "NaiveBayes";
        train(cls,path);
        test(cls,path);

        //arffToData();
        //renameExcel();
        //Evaluate e = new Evaluate(algorithms,tables,versions,users,variations);
        //e.run();
    }
    public static void train(Classifier cls, String path) throws Exception{
        // Train
        for (String t : tables) {
            Classify c = new Classify(users, cls, t, path,true);
            c.evaluate();
        }
    }
    public static void test(Classifier cls, String path) throws Exception{
        // Test
        for (String t : tables) {
            Classify c = new Classify(users, cls, t, path,false);
            c.evaluate();
        }
    }
    public static void arffToData() throws Exception {

        String importPath = "C:/Users/ARFF/";
        String exportPath = "C:/Users/DATA/";
        for (String t:tables) {
            for (String v:versions) {
                if (t != "call" && v == "e") break;
                for (String u:users){
                    for (String va:variations) {
                        String fileName = t + u + va + v;
                        Converter c = new Converter(importPath,fileName,exportPath);
                        c.arffToData();
                    }
                }
            }
        }
    }
    public static void renameExcel() throws Exception {
        String importPath = "C:/Users/EXCEL_FILES/";
        for (String a:algorithms) {
            for (String t:tables) {
                String fileName = a + "_" + "TRAIN"  + "_" + t.toUpperCase() ;
                Converter c = new Converter(importPath,fileName);
                c.renameExcel();
                fileName =  a + "_" + "TEST"  + "_" + t.toUpperCase() ;
                c = new Converter(importPath,fileName);
                c.renameExcel();
            }
        }
    }
}



