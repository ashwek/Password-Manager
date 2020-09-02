package com.nikhil.FirebaseFunction;

public class Creditstore {

    private String accNumber;
    private String bankName;
    private String cardNumber;
    private String expiryDate;
    private String cvv;
    private String extranote;

    public Creditstore(String accNumber, String bankName, String cardNumber, String expiryDate, String cvv, String extranote) {
        this.accNumber = accNumber;
        this.bankName = bankName;
        this.cardNumber = cardNumber;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
        this.extranote = extranote;
    }

    public Creditstore() {
    }


    public String getAccNumber() {
        return accNumber;
    }

    public void setAccNumber(String accNumber) {
        this.accNumber = accNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getExtranote() {
        return extranote;
    }

    public void setExtranote(String extranote) {
        this.extranote = extranote;
    }
}
