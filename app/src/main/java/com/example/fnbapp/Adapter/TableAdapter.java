package com.example.fnbapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fnbapp.Interface.Callback;
import com.example.fnbapp.Module.TableModule;
import com.example.fnbapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.ViewHolder> {
    Context context;
    List<TableModule> tableModuleList;

    public TableAdapter(Context context, List<TableModule> tableModuleList) {
        this.context = context;
        this.tableModuleList = tableModuleList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_table, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        View view = holder.getView();
        inflaterToView(view, position);

    }
    private void inflaterToView(View view, int position){
        ImageView table = view.findViewById(R.id.imageTable);
        TextView name = view.findViewById(R.id.txtTableName);
        name.setText(tableModuleList.get(position).getTableName());
        if (tableModuleList.get(position).getCount() == 2){
            table.setImageResource(R.drawable.ic_baseline_circle_24_gray);
        }
        if (tableModuleList.get(position).getCount() == 4){
            table.setImageResource(R.drawable.ic_baseline_crop_square_24);
        }
        if (tableModuleList.get(position).getCount() == 5){
            table.setImageResource(R.drawable.ic_baseline_calendar_view_day_24);
        }
    }

    @Override
    public int getItemCount() {
        return tableModuleList.size();
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
