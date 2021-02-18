package com.example.fnbapp.Interface;

import com.example.fnbapp.Module.ItemModule;

public interface CallBackItems {
    void onEditItem(ItemModule itemModule);
    void onChooseItem(ItemModule itemModule);
    void onUnChooseItem(ItemModule itemModule);
}

