package com.example.fnbapp.Module;

public class RestaurantInformation {

    String restaurantName;
    String ownerName;
    Long phoneNumber;
    String address;
    String provinceCity;
    String district;
    String businessModel;
    String businessAreas;
    String currency;
    String email;

    public RestaurantInformation() {
    }

    public RestaurantInformation(String restaurantName, String ownerName, Long phoneNumber, String address, String provinceCity, String district, String businessModel, String businessAreas, String currency, String email) {
        this.restaurantName = restaurantName;
        this.ownerName = ownerName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.provinceCity = provinceCity;
        this.district = district;
        this.businessModel = businessModel;
        this.businessAreas = businessAreas;
        this.currency = currency;
        this.email = email;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProvinceCity() {
        return provinceCity;
    }

    public void setProvinceCity(String provinceCity) {
        this.provinceCity = provinceCity;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getBusinessModel() {
        return businessModel;
    }

    public void setBusinessModel(String businessModel) {
        this.businessModel = businessModel;
    }

    public String getBusinessAreas() {
        return businessAreas;
    }

    public void setBusinessAreas(String businessAreas) {
        this.businessAreas = businessAreas;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
