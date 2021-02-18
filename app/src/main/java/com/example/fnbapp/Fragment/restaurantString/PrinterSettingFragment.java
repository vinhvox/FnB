package com.example.fnbapp.Fragment.restaurantString;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.fnbapp.MainActivity2;
import com.example.fnbapp.R;
import com.mazenrashed.printooth.Printooth;
import com.mazenrashed.printooth.ui.ScanningActivity;
import com.mazenrashed.printooth.utilities.Printing;
import com.mazenrashed.printooth.utilities.PrintingCallback;

public class PrinterSettingFragment extends Fragment implements PrintingCallback {
    View view;
CardView cardConnectPrinter;
Printing printing;
TextView     txtPrinter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.printer_fragment, container, false);
        Printooth.INSTANCE.init(view.getContext());
        init();
        return view;
    }
    private  void  init(){
        txtPrinter = view.findViewById(R.id.txtPrinter);
        cardConnectPrinter = view.findViewById(R.id.cardConnectPrinter);
        if (printing != null)
            printing.setPrintingCallback(this);
        cardConnectPrinter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Printooth.INSTANCE.hasPairedPrinter())
                    Printooth.INSTANCE.removeCurrentPrinter();
                else {
                    startActivityForResult(new Intent(view.getContext(), ScanningActivity.class), ScanningActivity.SCANNING_FOR_PRINTER);
                    changePairAndUnpair();
                }
            }
        });
    }

    private void changePairAndUnpair() {
        if (Printooth.INSTANCE.hasPairedPrinter())
            txtPrinter.setText(new StringBuilder("Unpair").append(Printooth.INSTANCE.getPairedPrinter().getName()).toString());
        else
            txtPrinter.setText("Pair with printer");
    }

    @Override
    public void connectingWithPrinter() {

    }

    @Override
    public void connectionFailed(String s) {

    }

    @Override
    public void onError(String s) {

    }

    @Override
    public void onMessage(String s) {

    }

    @Override
    public void printingOrderSentSuccessfully() {

    }
}
