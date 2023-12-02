package com.example.demo.metadata;

import info.debatty.java.stringsimilarity.Cosine;
import info.debatty.java.stringsimilarity.JaroWinkler;
import info.debatty.java.stringsimilarity.RatcliffObershelp;
import net.ricecode.similarity.JaroWinklerStrategy;
import net.ricecode.similarity.SimilarityStrategy;
import net.ricecode.similarity.StringSimilarityService;
import net.ricecode.similarity.StringSimilarityServiceImpl;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class metadataService {

    private final metadataRepository metadataRepository;
    public static final DecimalFormat df2 = new DecimalFormat( "#0.00" );
    @Autowired
    public metadataService(com.example.demo.metadata.metadataRepository metadataRepository) {
        this.metadataRepository = metadataRepository;
    }

    public List<Object[]> getMetdata() {
        return metadataRepository.findNameAndIsMasked();
    }

    public List<metdataResult> validateMasking(String excelPath){
        try {
            FileInputStream excelFile = new FileInputStream(excelPath);
            Workbook workbook = new XSSFWorkbook(excelFile);
            List<String> getTablesAndParameter = getTablesAndParameter(workbook,3);
            List<String> maskedAttributes = metadataRepository.findNameAndIsMaskedYes();
            List<String> unmaskedAttributes = metadataRepository.findNameAndIsMaskedNo();
            List<metdataResult> results =new ArrayList<>();
            String tableName = "";
            String parameterName = "";
            String masking = "";

            List<Integer>  count = metadataRepository.countNameAndIsMaskedYes();
            int maskedcount2 = count.get(0);
            System.out.println(maskedcount2);
            List<Integer>  countunmasked = metadataRepository.countNameAndIsMaskedNo();
            int unmaskedcount2 = countunmasked.get(0);
            System.out.println(unmaskedcount2);

            for(int i = 0 ; i < getTablesAndParameter.size() ; i++){
                double maskedfinalpercentage = 0 ;
                double maskedpercentage = 0;
                double counmaskedpercentage = 0;
                int maskedcount = 0;
                double unmaskedfinalpercentage = 0 ;
                double unmaskedpercentage = 0;
                double countunmaskedpercentage = 0;
                int unmaskedcount = 0;
                String resultType = "New Attribute (not exist in DB)";

                tableName = getTablesAndParameter.get(i);
                i++;
                parameterName = getTablesAndParameter.get(i);
                i++;
                masking = getTablesAndParameter.get(i);


                //masked
                for(int y = 0 ; y < maskedAttributes.size(); y++){
                    String maskedAttribute = maskedAttributes.get(y);
                    double resultOfSim = calculateSimilarity(parameterName.toLowerCase(Locale.ROOT),maskedAttribute.toLowerCase(Locale.ROOT));

                    if(resultOfSim > 50){
                        maskedpercentage = maskedpercentage + resultOfSim;
                        maskedcount++;
                    }

                }

                //unmasked
                for(int y = 0 ; y < unmaskedAttributes.size(); y++){
                    String unmaskedAttribute = unmaskedAttributes.get(y);
                    double resultOfSim = calculateSimilarity(parameterName.toLowerCase(Locale.ROOT),unmaskedAttribute.toLowerCase(Locale.ROOT));
                    if(resultOfSim > 50){
                        unmaskedpercentage = unmaskedpercentage + resultOfSim;
                        unmaskedcount++;
                    }
                }

                if(maskedpercentage != 0 && maskedcount != 0 ){
                    maskedfinalpercentage = maskedpercentage/maskedcount;
                    counmaskedpercentage = (double) maskedcount/maskedcount2;
                }
                if(unmaskedpercentage != 0 && unmaskedcount != 0 ){
                    unmaskedfinalpercentage = unmaskedpercentage/unmaskedcount;
                    countunmaskedpercentage = (double) unmaskedcount/unmaskedcount2;
                }
                if(maskedcount != 0 || unmaskedcount != 0){
                    if(counmaskedpercentage > countunmaskedpercentage){
                        resultType = "Masked";
                        results.add(new metdataResult(tableName,parameterName,resultType,df2.format(maskedfinalpercentage),masking));

                    }else{
                        resultType = "unMasked";
                        results.add(new metdataResult(tableName,parameterName,resultType,df2.format(unmaskedfinalpercentage),masking));

                    }
                }else{
                    results.add(new metdataResult(tableName,parameterName,resultType,null,masking));

                }


            }


            return results;



            //return getTablesAndParameter;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public List<metdataResult> validateMasking2(InputStream inputStream,String algorithm){
        try {
            System.out.println(algorithm);
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            List<String> getTablesAndParameter = getTablesAndParameter(workbook,3);
            List<String> maskedAttributes = metadataRepository.findNameAndIsMaskedYes();
            List<String> unmaskedAttributes = metadataRepository.findNameAndIsMaskedNo();
            List<metdataResult> results =new ArrayList<>();
            String tableName = "";
            String parameterName = "";
            String masking = "";

            List<Integer>  count = metadataRepository.countNameAndIsMaskedYes();
            int maskedcount2 = count.get(0);
            System.out.println(maskedcount2);
            List<Integer>  countunmasked = metadataRepository.countNameAndIsMaskedNo();
            int unmaskedcount2 = countunmasked.get(0);
            System.out.println(unmaskedcount2);

            for(int i = 0 ; i < getTablesAndParameter.size() ; i++){
                double maskedfinalpercentage = 0 ;
                double maskedpercentage = 0;
                double counmaskedpercentage = 0;
                int maskedcount = 0;
                double unmaskedfinalpercentage = 0 ;
                double unmaskedpercentage = 0;
                double countunmaskedpercentage = 0;
                int unmaskedcount = 0;
                String resultType = "N/A";

                tableName = getTablesAndParameter.get(i);
                i++;
                parameterName = getTablesAndParameter.get(i);
                i++;
                masking = getTablesAndParameter.get(i);


                //masked
                for(int y = 0 ; y < maskedAttributes.size(); y++){
                    String maskedAttribute = maskedAttributes.get(y);
                    double resultOfSim = algorithms(algorithm,parameterName.toLowerCase(Locale.ROOT),maskedAttribute.toLowerCase(Locale.ROOT));
                    if(resultOfSim > 50){
                        maskedpercentage = maskedpercentage + resultOfSim;
                        maskedcount++;
                    }

                }

                //unmasked
                for(int y = 0 ; y < unmaskedAttributes.size(); y++){
                    String unmaskedAttribute = unmaskedAttributes.get(y);
                    double resultOfSim = algorithms(algorithm,parameterName.toLowerCase(Locale.ROOT),unmaskedAttribute.toLowerCase(Locale.ROOT));
                    if(resultOfSim > 50){
                        unmaskedpercentage = unmaskedpercentage + resultOfSim;
                        unmaskedcount++;
                    }
                }

                if(maskedpercentage != 0 && maskedcount != 0 ){
                    maskedfinalpercentage = maskedpercentage/maskedcount;
                    counmaskedpercentage = (double) maskedcount/maskedcount2;
                }
                if(unmaskedpercentage != 0 && unmaskedcount != 0 ){
                    unmaskedfinalpercentage = unmaskedpercentage/unmaskedcount;
                    countunmaskedpercentage = (double) unmaskedcount/unmaskedcount2;
                }
                if(maskedcount != 0 || unmaskedcount != 0){
                    if(counmaskedpercentage > countunmaskedpercentage){
                        resultType = "Masked";
                        results.add(new metdataResult(tableName,parameterName,resultType,df2.format(maskedfinalpercentage),masking));

                    }else{
                        resultType = "not Masked";
                        results.add(new metdataResult(tableName,parameterName,resultType,df2.format(unmaskedfinalpercentage),masking));

                    }
                }else{
                    results.add(new metdataResult(tableName,parameterName,resultType,"N/A",masking));

                }


            }


            return results;



            //return getTablesAndParameter;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public List<String> getTablesAndParameter(Workbook workbook, int sheetNumber){

        List<String> tableParameterNames = new ArrayList<>();
        try{
            int tableColumnNumber = -1;
            int attributeColumnNumber = -1;
            int maskingColumnNumber = -1;
            int tableCount = 0;
            int attributeCount = 0;
            int maskingCount = 0;
            String tableName;
            String attributeName;
            String maskingValue;
            //get sheet
            Sheet sheet = workbook.getSheetAt(sheetNumber);
            int rowStart = sheet.getFirstRowNum();
            int rowEnd =sheet.getLastRowNum();
            //Rows
            for(int RowNumber = rowStart ; RowNumber <= rowEnd ; RowNumber++){
                Row r = sheet.getRow(RowNumber);
                if(r != null){
                    int lastColumn = Math.max(r.getLastCellNum(), 0);
                    //Cells
                    for (int cn = 0; cn < lastColumn; cn++) {
                        Cell c = r.getCell(cn, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        if (c != null) {
                            if(c.getStringCellValue().equals("Table Name")){
                                tableColumnNumber = cn;
                            }else{
                                if(c.getStringCellValue().equals("Attribute Name")){
                                    attributeColumnNumber = cn;
                                }else{
                                    if(c.getStringCellValue().equals("Masking Value")){
                                        maskingColumnNumber =cn;
                                        break;
                                    }else {
                                        if (tableColumnNumber != -1 && attributeColumnNumber != -1) {
                                            if (cn == tableColumnNumber) {
                                                tableName = c.getStringCellValue();
                                                tableParameterNames.add(tableName);
                                                tableCount++;
                                            } else {
                                                if (cn == attributeColumnNumber) {
                                                    attributeName = c.getStringCellValue();
                                                    tableParameterNames.add(attributeName);
                                                    attributeCount++;
                                                }else{
                                                    if (cn == maskingColumnNumber){
                                                        maskingValue = c.getStringCellValue();
                                                        tableParameterNames.add(maskingValue);
                                                        maskingCount++;

                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    // end of Cells
                }
            }
            // end of rows
            if(tableCount != attributeCount || tableCount != maskingCount){
                Exception e;
            }
        }catch (NullPointerException e){
            e.getCause();
        }
        return tableParameterNames;
    }

    public double calculateSimilarity(String str1, String str2) {

//        SimilarityStrategy strategy = new JaroWinklerStrategy();
//        StringSimilarityService service = new StringSimilarityServiceImpl(strategy);
//        double similarity = service.score(str1, str2);

        return 0;
    }

    public double algorithms(String algorithm,String str1 , String str2){
        RatcliffObershelp ro = new RatcliffObershelp();
        JaroWinkler jw = new JaroWinkler();
        Cosine cosine = new Cosine(2);

        if(algorithm.equals("Jaro–Winkler")){
            return  (jw.similarity(str1,str2)) *100;
        }else {
            if (algorithm.equals("Cosine")){
                Map<String, Integer> profile1 = cosine.getProfile(str1);
                Map<String, Integer> profile2 = cosine.getProfile(str2);

                return cosine.similarity(profile1,profile2)*100;
            }else{
                if(algorithm.equals("Ratcliff-Obershelp")){
                    return ro.similarity(str1,str2)*100;
                }if(algorithm.equals("Custom algorithm")){
                    int[][] dp = new int[str1.length() + 1][str2.length() + 1];

                    // Build the LCS matrix
                    for (int i = 1; i <= str1.length(); i++) {
                        for (int j = 1; j <= str2.length(); j++) {
                            if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
                                dp[i][j] = dp[i - 1][j - 1] + 1;
                            } else {
                                dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                            }
                        }
                    }

                    // Calculate similarity based on LCS
                    int lcsLength = dp[str1.length()][str2.length()];
                    double similarity;
                    if (Math.max(str1.length(), str2.length()) == 0) {
                        similarity = 0; // Avoid division by zero
                    } else {
                        similarity = (double) lcsLength / Math.max(str1.length(), str2.length()) * 100;
                    }

                    return similarity;
                }
            }
        }
        return 0;
    }

}
