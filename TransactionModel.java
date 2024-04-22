package com.example.myapplication;

public class TransactionModel {

    private String id;
    private String note;
    private String amount;
    private String type;
    private String date;
    private String category;

    public TransactionModel() {
    }


    public TransactionModel(String id, String note, String amount, String type, String date, String category) {
        this.id = id;
        this.note = note;
        this.amount = amount;
        this.type = type;
        this.date = date;
        this.category=category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}


