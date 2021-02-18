package com.example.fnbapp.Fragment.restaurantString;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.fnbapp.Module.RestaurantInformation;
import com.example.fnbapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class StoreInformationFragment  extends Fragment {
    View view;
    FirebaseDatabase database;
    DatabaseReference myRef;
    EditText edtRestaName, edtRestaPhone, edtRestaAddress, edtRestaProvinceCity, edtRestaDistrict, edtRestaBusinessModel, edtBusinessAreas, edtCurrency;
    TextView txtRestaCode;
    Button btnSaveInfo;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.store_information_fragment, container, false);
        init();

        return view;
    }

    private void init() {
        database = FirebaseDatabase.getInstance("https://fnb-foods-default-rtdb.firebaseio.com/");
        edtRestaName = view.findViewById(R.id.edtRestaName);
        txtRestaCode = view.findViewById(R.id.txtRestaCode);
        edtRestaAddress = view.findViewById(R.id.edtRestaAddress);
        edtRestaProvinceCity = view.findViewById(R.id.edtRestaProvinceCity);
        edtRestaDistrict = view.findViewById(R.id.edtRestaDistrict);
        edtRestaBusinessModel = view.findViewById(R.id.edtRestaBusinessModel);
        edtBusinessAreas = view.findViewById(R.id.edtBusinessAreas);
        edtCurrency = view.findViewById(R.id.edtCurrency);
        edtRestaPhone = view.findViewById(R.id.edtRestaPhone);
        btnSaveInfo = view.findViewById(R.id.btnSaveInfor);

    }

    @Override
    public void onResume() {
        super.onResume();
        getStoreInformation();
        saveInfoToDB();
    }

    private  void getStoreInformation(){
        myRef  = database.getReference("RestaurantInformation");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    RestaurantInformation restaurantInformation ;
                    for ( DataSnapshot dataSnapshot : snapshot.getChildren()){
                        restaurantInformation = dataSnapshot.getValue(RestaurantInformation.class);
                        setDataToView(restaurantInformation, dataSnapshot.getKey());
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    };
    private  void  setDataToView( RestaurantInformation restaurantInformation, String code){
        edtRestaName.setText(restaurantInformation.getRestaurantName());
        edtRestaPhone.setText(String.valueOf(restaurantInformation.getPhoneNumber()));
        edtRestaAddress.setText(restaurantInformation.getAddress());
        txtRestaCode.setText(code);
        edtRestaProvinceCity.setText(restaurantInformation.getProvinceCity());
        edtRestaDistrict.setText(restaurantInformation.getDistrict());
        edtBusinessAreas.setText(restaurantInformation.getBusinessAreas());
        edtRestaBusinessModel.setText(restaurantInformation.getBusinessModel());
        edtCurrency.setText(restaurantInformation.getCurrency());
    }
    private void saveInfoToDB(){
        btnSaveInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtRestaName.getText().toString().trim();
                String code = txtRestaCode.getText().toString().trim();
                Long phone = Long.parseLong(edtRestaPhone.getText().toString().trim());
                String province = edtRestaProvinceCity.getText().toString().trim();
                String district = edtRestaDistrict.getText().toString().trim();
                String model = edtRestaBusinessModel.getText().toString().trim();
                String areas = edtBusinessAreas.getText().toString().trim();
                String address = edtRestaAddress.getText().toString().trim();
                String currency = edtCurrency.getText().toString().trim();
               RestaurantInformation restaurantInformation = new RestaurantInformation(name, "", phone, address, province, district, model, areas, currency, "");
                Map<String, Object> map = new HashMap<>();
                map.put(code, restaurantInformation);
                myRef.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(view.getContext(), "Cập nhật thành công", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Log.e("Update::", task.getException().getMessage());
                        }
                    }
                });
            }
        });
    }

}
