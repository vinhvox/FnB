package com.example.fnbapp.Module;

public class ItemOrderModule {
    ItemModule itemModule;
    int amountItem;

    public ItemOrderModule(ItemModule itemModule, int amountItem) {
        this.itemModule = itemModule;
        this.amountItem = amountItem;
    }

    public ItemModule getItemModule() {
        return itemModule;
    }

    public void setItemModule(ItemModule itemModule) {
        this.itemModule = itemModule;
    }

    public int getAmountItem() {
        return amountItem;
    }

    public void setAmountItem(int amountItem) {
        this.amountItem = amountItem;
    }
}
