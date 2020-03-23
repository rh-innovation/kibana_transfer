package com.roberthalf.kibana.dao;

public enum KibanaObject {

    INDEX_PATTERN("index-pattern");

    private String value;

    private KibanaObject(String value){
        this.value = value;
    }

    public String getValue(){
        return this.value;
    }


}
