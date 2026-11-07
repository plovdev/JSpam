package com.plovdev.assis.db.models;

public class UserEntity {
    private String tgId;
    private String tgName;
    private String state;
    private String tarif;
    private String balance;

    public UserEntity(String tgId, String tgName, String state, String tarif, String balance) {
        this.tgId = tgId;
        this.tgName = tgName;
        this.state = state;
        this.tarif = tarif;
        this.balance = balance;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTgId() {
        return tgId;
    }

    public void setTgId(String tgId) {
        this.tgId = tgId;
    }

    public String getTgName() {
        return tgName;
    }

    public void setTgName(String tgName) {
        this.tgName = tgName;
    }

    public String getTarif() {
        return tarif;
    }

    public void setTarif(String tarif) {
        this.tarif = tarif;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
}