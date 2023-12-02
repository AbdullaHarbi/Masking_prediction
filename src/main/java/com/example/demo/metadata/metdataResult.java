package com.example.demo.metadata;


import lombok.Getter;
import lombok.Setter;

import java.text.DecimalFormat;

@Getter
@Setter
public class metdataResult {


    private String tablename;
    private String attributename;
    private String resultType;
    private String Percentage;
    private String maskingValueInSchema;


    public metdataResult() {
    }

    public metdataResult(String tablename, String attributename, String resultType, String Percentage,String maskingValueInSchema) {
        this.tablename = tablename;
        this.attributename = attributename;
        this.resultType = resultType;
        this.Percentage = Percentage;
        this.maskingValueInSchema = maskingValueInSchema;

    }

}
