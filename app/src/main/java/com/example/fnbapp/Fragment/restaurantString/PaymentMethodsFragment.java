package com.example.fnbapp.Fragment.restaurantString;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.fnbapp.Module.PaymentModule;
import com.example.fnbapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class PaymentMethodsFragment extends Fragment {
    View view;
    FirebaseDatabase database;
    DatabaseReference myRef;
Switch switchMethodCash, switchMethodVisa, switchMethodMoMo, switchZaloPay;
Button btnSaveMethod;
Boolean cash = false, visa = false, momo = false, zalo = false;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.payment_methods_fragment, container, false);
        database = FirebaseDatabase.getInstance("https://fnb-foods-default-rtdb.firebaseio.com/");
        init();
        return  view;
    }
    private void init(){
        switchMethodCash = view.findViewById(R.id.switchMethodCash);
        switchMethodVisa = view.findViewById(R.id.switchMethodVisa);
        switchMethodMoMo = view.findViewById(R.id.switchMethodMoMo);
        switchZaloPay = view.findViewById(R.id.switchZaloPay);
        btnSaveMethod = view.findViewById(R.id.btnSaveMethod);
        getPayment();
        switchMethodCash.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    cash = isChecked;
                }
                else
                    cash = false;
            }
        });
        switchMethodVisa.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    visa = isChecked;
                else
                    visa = false;
            }
        });
        switchMethodMoMo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    momo = isChecked;
                else
                    momo = false;
            }
        });
        switchZaloPay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    zalo = isChecked;
                else
                    zalo = false;
            }
        });
        btnSaveMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PaymentModule paymentModule = new PaymentModule("-MSTWFcsjYdvcP1GFyQd", cash, visa, momo, zalo);
                myRef = database.getReference("Payments");
                myRef.push().setValue(paymentModule).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(view.getContext(), "Cập nhật thành công", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(view.getContext(), "Cập nhật thất bại", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
    private void getPayment(){
        myRef = database.getReference("Payments");
        Query query = myRef.orderByChild("restCode").equalTo("-MSTWFcsjYdvcP1GFyQd");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    PaymentModule paymentModule;
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        paymentModule = dataSnapshot.getValue(PaymentModule.class);
                        switchMethodCash.setChecked(paymentModule.getCash());
                        switchMethodVisa.setChecked(paymentModule.getVisa());
                        switchMethodMoMo.setChecked(paymentModule.getMomo());
                        switchZaloPay.setChecked(paymentModule.getZalo());
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
