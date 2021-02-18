package com.example.fnbapp.Interface;

import com.example.fnbapp.Module.ItemModule;
import com.example.fnbapp.Module.ItemOrderModule;

public interface CallbackHome {
    public void onChooseMenu(String item);
    public void onChooseItemMenu(ItemModule itemModule);
    public void decreaseItem(ItemOrderModule itemOrderModule);
}
