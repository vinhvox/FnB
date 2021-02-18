package com.example.fnbapp.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fnbapp.Interface.CallbackAreas;
import com.example.fnbapp.Module.AreaModule;
import com.example.fnbapp.R;

import java.util.ArrayList;

public class AreaAdapter extends RecyclerView.Adapter<AreaAdapter.ViewHolder> {
    ArrayList<AreaModule>areaModules;
    Context context;
    CallbackAreas callback;
    int rowIndex = -1;

    public AreaAdapter(ArrayList<AreaModule> areaModules, Context context, CallbackAreas callback) {
        this.areaModules = areaModules;
        this.context = context;
        this.callback = callback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_area, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        View  view = holder.getView();
        inflateToView(view, position);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               callback.onChooseAreas(areaModules.get(position));
               rowIndex = position;
               notifyDataSetChanged();
            }
        });

    }
    private  void  inflateToView(View view, final int position){
        TextView txtAreaName = view.findViewById(R.id.txtAreaName);
        AreaModule areaModule = areaModules.get(position);
//        txtAreaName.setText(areaModule.getAreaName() + " ("+ areaModule.getTableModules().size()+")");
        txtAreaName.setText(areaModule.getAreaName() + " ()");

        if (rowIndex == position){
            txtAreaName.setTextColor(Color.RED);
        }
        else {
            txtAreaName.setTextColor(Color.BLACK);
        }
        Log.e("TAG", "inflateToView: "+ rowIndex + " "+ position);

    }

    @Override
    public int getItemCount() {
        return areaModules.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
        }

        public View getView() {
            return view;
        }
    }
}
