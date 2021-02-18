package com.example.fnbapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fnbapp.Module.RestaurantInformation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class RegistrationActivity extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference myRef;
    EditText edtName, edtOwnerName, edtPhoneNumber, edtMail, edtDistrict;
    Button btnReg;
String codeSys ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        database = FirebaseDatabase.getInstance("https://fnb-foods-default-rtdb.firebaseio.com/");
        init();
    }
    private  void init(){
        edtName  = findViewById(R.id.edtRestaurantName);
        edtOwnerName = findViewById(R.id.edtOwnerName);
        edtPhoneNumber = findViewById(R.id.edtPhoneNumber);
        edtMail = findViewById(R.id.edtEmail);
        edtDistrict = findViewById(R.id.edtDistrict);
        btnReg = findViewById(R.id.btnReg);
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });
    }
    private void saveData(){
        String name = edtName.getText().toString();
        String owner = edtOwnerName.getText().toString();
        final String phone = edtPhoneNumber.getText().toString();
        String mail = edtMail.getText().toString();
        String district = edtDistrict.getText().toString();
        if (name.isEmpty() || owner.isEmpty() || phone.isEmpty()){
            Toast.makeText(RegistrationActivity.this, "Vui lòng nhập thông tin", Toast.LENGTH_LONG).show();
        }
        else {
            final Long phoneNumber = Long.parseLong(phone);
            RestaurantInformation information = new RestaurantInformation(name, owner, phoneNumber, null, null, district, null, null, null, mail);
            myRef = database.getReference("RestaurantInformation");
            myRef.push().setValue(information).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){

                        Toast.makeText(RegistrationActivity.this, "Thanh Cong", Toast.LENGTH_LONG).show();
                        testSend(phone);
                    }
                    else {
                        Toast.makeText(RegistrationActivity.this, "Thanh Cong", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
    private void testSend(String phone){
        PhoneAuthProvider.getInstance()
                .verifyPhoneNumber("+84"+ phone,
                        60, TimeUnit.SECONDS,
                        (Activity) TaskExecutors.MAIN_THREAD,
                        mCallback);
        ;
    }
    private  PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            codeSys = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null){
                veriCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

        }
    };

    private void veriCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSys, code);
        siginA(credential);
    }

    private void siginA(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            startActivity( new Intent(RegistrationActivity.this, MainActivity2.class));
                        }
                    }
                });
    }

}