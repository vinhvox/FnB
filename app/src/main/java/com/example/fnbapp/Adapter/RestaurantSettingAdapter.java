package com.example.fnbapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fnbapp.Interface.Callback;
import com.example.fnbapp.Module.RestaurantSetting;
import com.example.fnbapp.R;

import java.util.ArrayList;

public class RestaurantSettingAdapter extends RecyclerView.Adapter<RestaurantSettingAdapter.ViewHolder> {
    Context context;
    ArrayList<RestaurantSetting> dataSetting;
    Callback callback;

    public RestaurantSettingAdapter(Context context, ArrayList<RestaurantSetting> dataSetting, Callback callback) {
        this.context = context;
        this.dataSetting = dataSetting;
        this.callback = callback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_restaurant_setting, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        View view = holder.getView();
        inflaterToViews(view, position);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onChooseStoreSetting(dataSetting.get(position).getId());
            }
        });

    }
    private  void inflaterToViews(View view, int position){
        ImageView imageView = view.findViewById(R.id.imgStoreSetting);
        TextView textView = view.findViewById(R.id.txtTitleSetting);
        RestaurantSetting restaurantSetting = dataSetting.get(position);
        imageView.setImageResource(restaurantSetting.getIcon());
        textView.setText(restaurantSetting.getTitle());
    }

    @Override
    public int getItemCount() {
        return dataSetting.size();
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
