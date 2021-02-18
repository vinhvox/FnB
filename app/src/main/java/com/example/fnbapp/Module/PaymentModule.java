package com.example.fnbapp.Module;

public class PaymentModule {
    String restCode;
    Boolean cash;
    Boolean visa;
    Boolean momo;
    Boolean zalo;

    public PaymentModule() {
    }

    public PaymentModule(String restCode, Boolean cash, Boolean visa, Boolean momo, Boolean zalo) {
        this.restCode = restCode;
        this.cash = cash;
        this.visa = visa;
        this.momo = momo;
        this.zalo = zalo;
    }

    public String getRestCode() {
        return restCode;
    }

    public void setRestCode(String restCode) {
        this.restCode = restCode;
    }

    public Boolean getCash() {
        return cash;
    }

    public void setCash(Boolean cash) {
        this.cash = cash;
    }

    public Boolean getVisa() {
        return visa;
    }

    public void setVisa(Boolean visa) {
        this.visa = visa;
    }

    public Boolean getMomo() {
        return momo;
    }

    public void setMomo(Boolean momo) {
        this.momo = momo;
    }

    public Boolean getZalo() {
        return zalo;
    }

    public void setZalo(Boolean zalo) {
        this.zalo = zalo;
    }
}
