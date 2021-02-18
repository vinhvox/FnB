package com.example.fnbapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fnbapp.Interface.CallbackHome;
import com.example.fnbapp.Module.ItemModule;
import com.example.fnbapp.Module.ItemOrderModule;
import com.example.fnbapp.R;

import java.text.DecimalFormat;
import java.util.List;

public class HomeOrderAdapter extends RecyclerView.Adapter<HomeOrderAdapter.ViewHolder> {
    Context context;
    List<ItemOrderModule> itemModuleList;
    CallbackHome callbackHome;

    public HomeOrderAdapter(Context context, List<ItemOrderModule> itemModuleList, CallbackHome callbackHome) {
        this.context = context;
        this.itemModuleList = itemModuleList;
        this.callbackHome = callbackHome;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = R.layout.items_order;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(layoutId, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        View view =holder.getView();
        inflaterToView(view, position);

    }
    private  void inflaterToView(View view, final int position){
        TextView itemName = view.findViewById(R.id.itemsOrderName);
        TextView itemPrice = view.findViewById(R.id.itemsOrderPrice);
        final TextView amountItem = view.findViewById(R.id.amountItem);
        ImageView increaseItem = view.findViewById(R.id.increaseItem);
        ImageView decreaseItem = view.findViewById(R.id.decreaseItem);

        final ItemOrderModule itemOrderModule = itemModuleList.get(position);
        ItemModule itemModule = itemOrderModule.getItemModule();
        itemName.setText(itemModule.getItemName());
        amountItem.setText(itemOrderModule.getAmountItem()+"");
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        itemPrice.setText(formatter.format(itemModule.getPriceItem() * itemOrderModule.getAmountItem())+" VND");
        increaseItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callbackHome.onChooseItemMenu(itemModuleList.get(position).getItemModule());
            }
        });
        decreaseItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callbackHome.decreaseItem(itemOrderModule);
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
            this.view  = itemView;
        }

        public View getView() {
            return view;
        }
    }
}
