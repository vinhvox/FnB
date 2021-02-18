package com.example.fnbapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.fnbapp.Interface.CallBackItems;
import com.example.fnbapp.Module.ItemModule;
import com.example.fnbapp.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;
import java.util.zip.Inflater;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {
    Context context;
    List<ItemModule> itemModuleList;
    CallBackItems callback;
    Boolean checkAll;

    public ItemsAdapter(Context context, List<ItemModule> itemModuleList, CallBackItems callback, Boolean checkAll) {
        this.context = context;
        this.itemModuleList = itemModuleList;
        this.callback = callback;
        this.checkAll = checkAll;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = R.layout.row_items;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(layoutId, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        View view = holder.getView();
        inflaterToView(view, position);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onEditItem(itemModuleList.get(position));
            }
        });

    }
    private void inflaterToView(View view, int position){

        CheckBox checkBoxItem = view.findViewById(R.id.checkboxItem);
        ImageView imageViewItem = view.findViewById(R.id.itemImage);
        TextView txtItemName = view.findViewById(R.id.itemName);
        TextView txtCategory = view.findViewById(R.id.itemCategory);
        TextView txtPriceItem = view.findViewById(R.id.priceItem);
        final ItemModule itemModule = itemModuleList.get(position);
            checkBoxItem.setChecked(checkAll);
        Picasso.get()
                .load(itemModule.getImageItem())
                .placeholder(R.drawable.street_food)
                .error(R.drawable.error_cloud)
                .into(imageViewItem);
        txtItemName.setText(itemModule.getItemName());
        txtCategory.setText(itemModule.getCategoryItem());
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        txtPriceItem.setText(formatter.format(itemModule.getPriceItem())+" VND");
        checkBoxItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    callback.onChooseItem(itemModule);
                }
                else {
                    callback.onUnChooseItem(itemModule);
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return itemModuleList.size();
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
