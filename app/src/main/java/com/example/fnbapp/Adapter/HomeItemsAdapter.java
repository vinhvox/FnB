package com.example.fnbapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fnbapp.Interface.CallBackItems;
import com.example.fnbapp.Interface.CallbackHome;
import com.example.fnbapp.Interface.CallbackOrder;
import com.example.fnbapp.Module.ItemModule;
import com.example.fnbapp.Module.ItemOrderModule;
import com.example.fnbapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HomeItemsAdapter extends RecyclerView.Adapter<HomeItemsAdapter.ViewHolder> {
    Context context;
    List<ItemModule> itemModuleList;
    CallbackHome callBackItems;
    List<ItemOrderModule> itemOrderModules;

    public HomeItemsAdapter(Context context, List<ItemModule> itemModuleList, CallbackHome callBackItems) {
        this.context = context;
        this.itemModuleList = itemModuleList;
        this.callBackItems = callBackItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = R.layout.items_home;
        itemOrderModules = new ArrayList<>();
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
                ItemModule itemModule = itemModuleList.get(position);
//                for (int i = 0 ; i < itemOrderModules.size(); i++){
//                    if (itemOrderModules.get(i).getItemModule().equals(itemModule)){
////                        int pos = itemOrderModules.indexOf(itemModule);
//                        ItemOrderModule itemOrderModule = new ItemOrderModule(itemModule, itemOrderModules.get(i).getAmountItem() +1);
//                        itemOrderModules.set(i, itemOrderModule);
//                    }
//                }
//                itemOrderModules.add(new ItemOrderModule(itemModule, 1));
//                callBackItems.onOrder(itemOrderModules);
                callBackItems.onChooseItemMenu(itemModule);
            }
        });

    }
    private void inflaterToView(View view, int position){
        ImageView imageView = view.findViewById(R.id.imageItemHome);
        TextView textView = view.findViewById(R.id.txtItemNameHome);
        ItemModule itemModule = itemModuleList.get(position);
        Picasso.get().load(itemModule.getImageItem()).into(imageView);
        textView.setText(itemModule.getItemName());
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
