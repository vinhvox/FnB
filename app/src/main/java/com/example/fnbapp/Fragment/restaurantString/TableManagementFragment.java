package com.example.fnbapp.Fragment.restaurantString;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fnbapp.Adapter.AreaAdapter;
import com.example.fnbapp.Adapter.TableAdapter;
import com.example.fnbapp.Interface.Callback;
import com.example.fnbapp.Interface.CallbackAreas;
import com.example.fnbapp.Module.AreaModule;
import com.example.fnbapp.Module.ItemModule;
import com.example.fnbapp.Module.TableModule;
import com.example.fnbapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TableManagementFragment extends Fragment implements CallbackAreas {
    View view;
    ArrayList<AreaModule> areaModules;
    RecyclerView recyclerViewArea, recyclerViewTable;
    FirebaseDatabase database;
    DatabaseReference myRef;
    Button btnAddAreas;
    List<TableModule> tableModules;
    TableAdapter tableAdapter;
    int countTable;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.table_management_fragment, container, false);
        database = FirebaseDatabase.getInstance("https://fnb-foods-default-rtdb.firebaseio.com/");

        init();

        return  view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    private void init(){
        btnAddAreas = view.findViewById(R.id.btnAddAreas);

        btnAddAreas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(view.getContext());
                dialog.setContentView(R.layout.dialog_create_areas);
                final EditText edtAreaName = dialog.findViewById(R.id.edtAreaName);
                final EditText edtCountTable = dialog.findViewById(R.id.edtCountTable);
                final EditText edtNumberStart = dialog.findViewById(R.id.edtNumberStart);
                RadioButton rbtOption1 = dialog.findViewById(R.id.rbtOption1);
                RadioButton rbtOption2 = dialog.findViewById(R.id.rbtOption2);
                RadioButton rbtOption3 = dialog.findViewById(R.id.rbtOption3);
                Button btnSave = dialog.findViewById(R.id.btnSaveAreas);
                rbtOption1.setOnCheckedChangeListener(listener);
                rbtOption2.setOnCheckedChangeListener(listener);
                rbtOption3.setOnCheckedChangeListener(listener);
                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = edtAreaName.getText().toString().trim();
                        int count = Integer.parseInt(edtCountTable.getText().toString().trim());
                        int start = Integer.parseInt(edtNumberStart.getText().toString().trim());
                        ArrayList<TableModule> tableModules1 = new ArrayList<>();
                        for (int i = start ; i < start+ count; i++){
                            tableModules1.add(new TableModule(String.valueOf(i), countTable));
                        }

                        AreaModule areaModule1 = new AreaModule(name, tableModules1);
                        myRef = database.getReference("Areas");
                        myRef.push().setValue(areaModule1).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(view.getContext(),"Them Thanhf coong", Toast.LENGTH_LONG).show();
                                    onResume(); 
                                }
                                else {
                                    Toast.makeText(view.getContext(),"Them Thaast baji", Toast.LENGTH_LONG).show();

                                }
                            }
                        });
                    }
                });
                dialog.show();

            }
        });
    }
    CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked){
               int id = buttonView.getId();
               switch (id){
                   case R.id.rbtOption1:
                       countTable = 2;
                       break;
                   case R.id.rbtOption2:
                       countTable = 4;
                       break;
                   case R.id.rbtOption3:
                       countTable =5;
                       break;
                   default:
                       countTable = 2;

               }
            }
        }
    };
    private  void setUpView(ArrayList<AreaModule> areaModules){
        recyclerViewArea = view.findViewById(R.id.recyclerViewArea);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerViewArea.setLayoutManager(layoutManager);
        AreaAdapter areaAdapter = new AreaAdapter(areaModules, view.getContext(), this );
        recyclerViewArea.setAdapter(areaAdapter);

    }
    private void setRecyclerViewTable(ArrayList<TableModule> table){
        recyclerViewTable = view.findViewById(R.id.recyclerViewTable);
        GridLayoutManager layoutManager = new GridLayoutManager(view.getContext(), 3);
        recyclerViewTable.setLayoutManager(layoutManager);
        tableAdapter = new TableAdapter(view.getContext(), table);
        recyclerViewTable.setAdapter(tableAdapter);
    }
    private void getData(){
        areaModules = new ArrayList<>();
        myRef = database.getReference("Areas");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    AreaModule areaModule;
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        areaModule = dataSnapshot.getValue(AreaModule.class);
                        areaModules.add(areaModule);
                    }
                    setUpView(areaModules);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public void onChooseAreas(AreaModule areaModule) {
        setRecyclerViewTable((ArrayList<TableModule>) areaModule.getTableModules());
        Log.e("TAG", "onChooseAreas: "+ areaModule.getTableModules() );
    }
}
