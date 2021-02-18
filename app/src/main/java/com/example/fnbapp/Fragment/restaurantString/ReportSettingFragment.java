package com.example.fnbapp.Fragment.restaurantString;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.fnbapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ReportSettingFragment extends Fragment {
    View view;
    FirebaseDatabase database;
    DatabaseReference myRef;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.report_setting_fragment, container, false);
        database = FirebaseDatabase.getInstance("https://fnb-foods-default-rtdb.firebaseio.com/");
        init();
        return view;
    }
    private void init(){
        final EditText edtHour = view.findViewById(R.id.edtReportHour);
        final EditText edtMine = view.findViewById(R.id.edtReportMine);
        Button btnSave = view.findViewById(R.id.btnSaveReport);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                String hour = edtHour.getText().toString().trim();
                String mine = edtMine.getText().toString().trim();
                String value = hour + ":"+ mine;
                myRef = database.getReference("ReportSetting");
                myRef.setValue(value).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(view.getContext(), "Cài đặt báo cáo thành công", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(view.getContext(), "Cài đặt báo cáo thất bại", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

}
