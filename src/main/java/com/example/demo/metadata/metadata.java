package com.example.demo.metadata;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Attribute_data1")
public class metadata {

    @Column(columnDefinition="nvarchar(255)")
    private String Agency_Short_Name;
    @Column(columnDefinition="nvarchar(255)")
    private String Dataset_Code	;
    @Column(columnDefinition="nvarchar(255)")
    private String Dataset_Name	;
    @Column(columnDefinition="nvarchar(4000)")
    private String Dataset_Description	;
    @Column(columnDefinition="nvarchar(255)")
    private String Datastore_Name	;
    @Column(columnDefinition="nvarchar(4000)")
    private String Datastore_Description	;
    @Column(columnDefinition="nvarchar(255)")
    @Id
    private String Attribute_Code	;
    @Column(columnDefinition="nvarchar(255)")
    private String Attribute_Name	;
    @Column(name = "IsMasked",columnDefinition="tinyint" )
    private int IsMasked	;


    public metadata() {
    }

    public void metadata(String attribute_Name, int isMasked) {
        Attribute_Name = attribute_Name;
        IsMasked = isMasked;
    }

    public metadata(String agency_Short_Name, String dataset_Code, String dataset_Name, String dataset_Description,
                    String datastore_Name, String datastore_Description, String attribute_Code, String attribute_Name, int isMasked) {
        Agency_Short_Name = agency_Short_Name;
        Dataset_Code = dataset_Code;
        Dataset_Name = dataset_Name;
        Dataset_Description = dataset_Description;
        Datastore_Name = datastore_Name;
        Datastore_Description = datastore_Description;
        Attribute_Code = attribute_Code;
        Attribute_Name = attribute_Name;
        IsMasked = isMasked;
    }

}
