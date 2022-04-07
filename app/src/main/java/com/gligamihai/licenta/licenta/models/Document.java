package com.gligamihai.licenta.licenta.models;

public class Document {
    private String type;


    public Document(){
    }

    public Document(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
