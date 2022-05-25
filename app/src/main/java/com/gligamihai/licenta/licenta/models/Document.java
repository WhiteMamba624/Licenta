package com.gligamihai.licenta.licenta.models;

public class Document {
    private String type;
    private String plateNumber;
    private String vinNumber;
    private String expiryDate;
    private String deviceToken;

    public Document(){
    }

    public Document(String type, String plateNumber, String vinNumber, String expiryDate, String deviceToken) {
        this.type = type;
        this.plateNumber=plateNumber;
        this.vinNumber=vinNumber;
        this.expiryDate=expiryDate;
        this.deviceToken = deviceToken;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getVinNumber() {
        return vinNumber;
    }

    public void setVinNumber(String vinNumber) {
        this.vinNumber = vinNumber;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
