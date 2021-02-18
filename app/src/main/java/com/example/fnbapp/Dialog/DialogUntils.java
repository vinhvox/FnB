package com.example.fnbapp.Dialog;

import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.fnbapp.R;

public class DialogUntils {
    Dialog dialog;
    public void addCategory(final View view){

    }
    public  void addMenu(View view){
        dialog = new Dialog(view.getContext());
        dialog.setContentView(R.layout.dialog_add_menu);
        dialog.show();
    }
}
