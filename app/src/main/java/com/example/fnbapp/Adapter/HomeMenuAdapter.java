package com.example.fnbapp.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fnbapp.Interface.Callback;
import com.example.fnbapp.Interface.CallbackHome;
import com.example.fnbapp.R;

import java.util.List;

public class HomeMenuAdapter extends RecyclerView.Adapter<HomeMenuAdapter.ViewHolder> {
    Context context;
    List<String> listMenu;
    CallbackHome callbackHome;
    int rowIndex = -1;
    public HomeMenuAdapter(Context context, List<String> listMenu, CallbackHome callbackHome) {
        this.context = context;
        this.listMenu = listMenu;
        this.callbackHome = callbackHome;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = R.layout.row_menu_item;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(layoutId, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        View view = holder.getView();
        inflateToView(view, position);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rowIndex = position;
                callbackHome.onChooseMenu(listMenu.get(position));
            }
        });
    }
    private void inflateToView(View view, int position){
        TextView textView = view.findViewById(R.id.txtMenuItem);
        RelativeLayout relativeLayout = view.findViewById(R.id.relativeLayoutMenuItem);
        textView.setText(listMenu.get(position));
        if (rowIndex == position){
            textView.setTextColor(Color.RED);
            relativeLayout.setBackgroundResource(R.drawable.menu_item_choose);
        }
        else {
            relativeLayout.setBackgroundResource(R.drawable.menu_item_no_choose);
        }
    }
    @Override
    public int getItemCount() {
        return listMenu.size();
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
