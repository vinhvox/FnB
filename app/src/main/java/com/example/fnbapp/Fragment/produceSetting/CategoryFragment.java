package com.example.fnbapp.Fragment.produceSetting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fnbapp.Adapter.CategoryAdapter;
import com.example.fnbapp.Adapter.MenuAdapter;
import com.example.fnbapp.Interface.Callback;
import com.example.fnbapp.Module.ItemModule;
import com.example.fnbapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends Fragment implements Callback {
    View view;
    FirebaseDatabase database;
    DatabaseReference myRef;
    List<String> categoryList;
    CategoryAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.category_fragment, container, false);
        database = FirebaseDatabase.getInstance("https://fnb-foods-default-rtdb.firebaseio.com/");
        getDataCategory();
        return  view;
    }
    private void getDataCategory(){
        categoryList = new ArrayList<>();
        myRef = database.getReference("Category");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        categoryList.add(dataSnapshot.getValue().toString());
                    }
                    setupRecyclerView(categoryList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private  void  setupRecyclerView(List<String> list){
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewCategory);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
         RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
        adapter = new CategoryAdapter(view.getContext(), list, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

    }

    @Override
    public void onChooseStoreSetting(int id) {

    }


}
