package com.taylanakbas;

import jxl.Sheet;
import jxl.Workbook;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class Converter {
    public ArrayList<String> attributes = new ArrayList<>();
    public ArrayList<String> instances = new ArrayList<>();
    public int numOfAttributes;
    public int numOfInstances;
    public String fileName;
    public String filePath;
    public String exportPath;

    public Converter(String filePath, String fileName, String exportPath) throws Exception {
        this.exportPath = exportPath;
        this.fileName = fileName;
        this.filePath = filePath;
        readArff();
    }
    public Converter(String filePath, String fileName){
        this.fileName = fileName;
        this.filePath = filePath;
    }
    public Converter(String filePath){
        this.filePath = filePath;
    }
    public HashMap<String, Result> readXls(HashMap<String, Result> results, String version) throws Exception {
        // Reads .xls file for table evaluation (all system)
        Workbook workbook;
        workbook = Workbook.getWorkbook(new File(this.filePath));
        if (version == "") version = "ALL";
        Sheet sheet = workbook.getSheet(version.toUpperCase());
        Result result = new Result();
        result.avgTP = Double.parseDouble(sheet.getCell(16,2).getContents());
        results.put(version,result);
        workbook.close();
        return results;
    }
    public  HashMap<String,Double> readXls(String version, int numOfUsers, int numOfVariations) throws Exception {
        // Reads .xls file for table evaluation (individually)
        Workbook workbook;
        workbook = Workbook.getWorkbook(new File(this.filePath));
        Sheet sheet = workbook.getSheet(version.toUpperCase());
        HashMap<String,Double> users = new HashMap<>();
        for (int i = 0; i < numOfUsers ; i++) {
            for (int j = 1; j <= numOfVariations; j++) {
                users.put(sheet.getCell(j,(i*4)).getContents(),Double.parseDouble(sheet.getCell(j,2 + (i * 4)).getContents()));
            }
        }
        workbook.close();
        return users;
    }
    private void readArff() throws Exception {
        // Reads .arff file
        String fullPath = this.filePath + this.fileName + ".arff";
        System.out.println(fullPath);
        ConverterUtils.DataSource source = new ConverterUtils.DataSource(fullPath);
        Instances data = source.getDataSet();
        if (data.classIndex() == -1)
            data.setClassIndex(data.numAttributes() - 1);
        this.numOfAttributes = data.numAttributes();
        this.numOfInstances = data.numInstances();
        for (int i = 0; i < this.numOfAttributes ; i++) {
            String attribute = data.attribute(i).name();
            this.attributes.add(attribute);
        }
        for (int i = 0; i < this.numOfInstances ; i++) {
            String instance = data.instance(i).toString();
            this.instances.add(instance);
        }
    }
    public void renameExcel(){
        // Example :
        //  BayesNet_TEST_ACTIVITY.xls -> BayesNetactivityt.xls
        //  BayesNet_TRAIN_ACTIVITY.xls -> BayesNetactivity.xls
        String fullPath = this.filePath + this.fileName + ".xls";
        String algo, table, test, newName, delimiter = "_", format = ".xls";
        String[] temp;

        temp = this.fileName.split(delimiter);
        algo = temp[0];
        table = temp[2];
        test =  temp[1] == "TEST" ? "t"  : "";
        newName = algo + table.toLowerCase() + test + format; // You can modify this line according to your needs.

        File oldfile = new File(fullPath);
        File newfile = new File(this.filePath + newName );
        try{
            if (oldfile.renameTo(newfile)) {
                System.out.println("Success");
            }else{
                System.out.println("Error");
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }
    public void arffToData() throws IOException {
        // Converts an .arff to .data
        ArrayList<String> data = new ArrayList<>();
        data.add(0,Integer.toString(this.numOfAttributes - 1));
        String attributeLine = "#n ";
        for (String att: this.attributes) {
            attributeLine += att + " ";
        }
        data.add(1, attributeLine);
        String instanceLine = "";
        int i = 2;
        for (String instance : this.instances){
            instanceLine = instance.replace(",", "\t");
            data.add(i, instanceLine);
            i++;
        }
        Path file = Paths.get(exportPath + fileName + ".data");
        Files.write(file, data, StandardCharsets.UTF_8);
    }
}