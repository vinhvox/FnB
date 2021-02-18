package com.example.fnbapp.Module;

public class ItemModule {
    String itemName;
    Long priceItem;
    String imageItem;
    String unitItem;
    String categoryItem;
    String menuItem;
    Boolean isWeight;
    Boolean isWarehouseLink;
    String warehouse;
    String Description;
    String key;

    public ItemModule() {
    }

    public ItemModule(String itemName, Long priceItem, String imageItem, String unitItem, String categoryItem, String menuItem, Boolean isWeight, Boolean isWarehouseLink, String warehouse, String description) {
        this.itemName = itemName;
        this.priceItem = priceItem;
        this.imageItem = imageItem;
        this.unitItem = unitItem;
        this.categoryItem = categoryItem;
        this.menuItem = menuItem;
        this.isWeight = isWeight;
        this.isWarehouseLink = isWarehouseLink;
        this.warehouse = warehouse;
        Description = description;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Long getPriceItem() {
        return priceItem;
    }

    public void setPriceItem(Long priceItem) {
        this.priceItem = priceItem;
    }

    public String getImageItem() {
        return imageItem;
    }

    public void setImageItem(String imageItem) {
        this.imageItem = imageItem;
    }

    public String getUnitItem() {
        return unitItem;
    }

    public void setUnitItem(String unitItem) {
        this.unitItem = unitItem;
    }

    public String getCategoryItem() {
        return categoryItem;
    }

    public void setCategoryItem(String categoryItem) {
        this.categoryItem = categoryItem;
    }

    public String getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(String menuItem) {
        this.menuItem = menuItem;
    }

    public Boolean getWeight() {
        return isWeight;
    }

    public void setWeight(Boolean weight) {
        isWeight = weight;
    }

    public Boolean getWarehouseLink() {
        return isWarehouseLink;
    }

    public void setWarehouseLink(Boolean warehouseLink) {
        isWarehouseLink = warehouseLink;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
