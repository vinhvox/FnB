package com.example.fnbapp.Fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fnbapp.Adapter.HomeItemsAdapter;
import com.example.fnbapp.Adapter.HomeMenuAdapter;
import com.example.fnbapp.Adapter.HomeOrderAdapter;
import com.example.fnbapp.CONSTRAINT;
import com.example.fnbapp.Interface.CallbackHome;
import com.example.fnbapp.Interface.CallbackOrder;
import com.example.fnbapp.Module.CustomerModule;
import com.example.fnbapp.Module.ItemModule;
import com.example.fnbapp.Module.ItemOrderModule;
import com.example.fnbapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements CallbackOrder, CallbackHome {
    View view;
    FirebaseDatabase database;
    DatabaseReference myRef;
    List<String> listMenu;
    List<ItemModule> itemModuleList;
    private final static String TAG = HomeFragment.class.getName();
    RelativeLayout showAll;
    List<ItemOrderModule> itemsOrder;
    List<ItemModule> itemChoose;
    TextView numberOfGoods, moneyOnGoods, txtTotal;
    Long money=0l;
    RecyclerView recyclerViewItem;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_fragment, container, false);
        database = FirebaseDatabase.getInstance(CONSTRAINT.INSTANCE);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        recyclerViewItem  = view.findViewById(R.id.recyclerViewItemOrder);
        getDataItems("All");
        init();
        return  view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getDataMenuItem();
        onSaveOrder();
    }
    private void init(){
        itemChoose = new ArrayList<>();
        itemsOrder = new ArrayList<>();
        numberOfGoods = view.findViewById(R.id.numberOfGoods);
        moneyOnGoods = view.findViewById(R.id.moneyOnGoods);
        txtTotal = view.findViewById(R.id.txtTotal);
        showAll = view.findViewById(R.id.showAllItem);
        showAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataItems("All");
            }
        });
    }
    private void onSaveOrder(){
        Button button = view.findViewById(R.id.btnSaveOrder);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogSaveOrder();
            }
        });
    }

    private void openDialogSaveOrder() {
        Dialog dialog = new Dialog(view.getContext());
        dialog.setContentView(R.layout.dialog_save_order);

        TextView txtCustomer = dialog.findViewById(R.id.edtCustomer);
        txtCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogListCustomer();
            }
        });
        dialog.show();
    }
    private void openDialogListCustomer(){
        Dialog dialog = new Dialog(view.getContext());
        dialog.setContentView(R.layout.dialog_customer_list);
        dialog.show();
    }

    private void openDialogCustomer() {
        Dialog dialog = new Dialog(view.getContext());
        dialog.setContentView(R.layout.dialog_customer);
        final EditText edtFirstName = dialog.findViewById(R.id.edtFirstNameCustomer);
        final EditText edtName = dialog.findViewById(R.id.edtCustomerName);
        final EditText edtPhone = dialog.findViewById(R.id.edtCustomerPhone);
        final EditText edtEmail = dialog.findViewById(R.id.edtCustomerEmail);
        final EditText edtBirthDay = dialog.findViewById(R.id.edtCustomerEmail);
        final EditText edtSex = dialog.findViewById(R.id.edtSexCustomer);
        final EditText edtAddress = dialog.findViewById(R.id.edtAddressCustomer);
        final EditText edtNote  = dialog.findViewById(R.id.edtNoteCustomer);
        Button button = dialog.findViewById(R.id.btnAddCustomerA);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName, name, email, birthDay,  sex, address, note;
                Long phone;
                firstName = edtFirstName.getText().toString().trim();
                name = edtName.getText().toString().trim();
                phone = Long.parseLong(edtPhone.getText().toString().trim());
                email = edtEmail.getText().toString().trim();
                birthDay = edtBirthDay.getText().toString().trim();
                sex = edtSex.getText().toString().trim();
                address = edtAddress.getText().toString().trim();
                note = edtNote.getText().toString().trim();
                if (name == null){
                    edtName.setError("Không được bỏ sót");
                }
                if (phone == null || edtPhone.getText().toString().length() < 10 && edtPhone.getText().toString().length() > 10 ){
                    edtPhone.setError("Số điện thoại không được bỏ sót");
                }
                CustomerModule customerModule = new CustomerModule(firstName, name, phone, email, birthDay, sex, address, note);
                onSaveCustomer(customerModule);
            }
        });
        dialog.show();
    }

    private void onSaveCustomer(CustomerModule customerModule) {
        myRef = database.getReference("Customer");
        myRef.push().setValue(customerModule).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(view.getContext(), "Thêm khách hoàng thành công", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(view.getContext(), "Thêm khách hàng thất bại", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void getDataItems(final String menu){
        itemModuleList = new ArrayList<>();
        myRef = database.getReference("items");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    ItemModule itemModule;
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        itemModule = dataSnapshot.getValue(ItemModule.class);
                        if (menu.equals("All")){
                            itemModuleList.add(itemModule);
                        }
                        if (itemModule.getMenuItem().equals(menu)){
                            itemModuleList.add(itemModule);
                        }
                    }
                    setupRecyclerItems(itemModuleList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
}
    private void setupRecyclerItems(List<ItemModule> itemModules){
        RecyclerView recyclerView = view.findViewById(R.id.recyclerItems);
        GridLayoutManager layoutManager = new GridLayoutManager(view.getContext(), 3);
        recyclerView.setLayoutManager(layoutManager);
        HomeItemsAdapter adapter = new HomeItemsAdapter(view.getContext(), itemModules, this );
        recyclerView.setAdapter(adapter);


    }
    private void getDataMenuItem(){
        listMenu = new ArrayList<>();
        myRef = database.getReference("Menu");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        listMenu.add(dataSnapshot.getValue().toString());
                    }
                    setupRecyclerViewMenu(listMenu);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private  void setupRecyclerViewMenu(List<String> list){
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewMenuItem);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        HomeMenuAdapter adapter = new HomeMenuAdapter(view.getContext(), list, this);
        recyclerView.setAdapter(adapter);
    }
    private void setupRecyclerItemsOrder(List<ItemOrderModule> itemModules){

        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerViewItem.setLayoutManager(layoutManager);
        HomeOrderAdapter adapter = new HomeOrderAdapter(view.getContext(), itemModules, this);
        recyclerViewItem.setAdapter(adapter);

    }

    @Override
    public void onChooseMenu(String item) {
        getDataItems(item);
    }
    @Override
    public void onChooseItemMenu(ItemModule itemModule) {
        if (itemChoose.contains(itemModule)){
            Log.e(TAG, "onChooseItemMenu: exists" );
            for( int i =0 ; i < itemsOrder.size() ; i++){
                if (itemsOrder.get(i).getItemModule().equals(itemModule)){
                    ItemOrderModule itemOrderModule = new ItemOrderModule(itemModule, itemsOrder.get(i).getAmountItem() +1);
                    itemsOrder.set(i, itemOrderModule );
                }
            }
        }
        else {
            Log.e(TAG, "onChooseItemMenu: not exists" );
            itemChoose.add(itemModule);
            itemsOrder.add(new ItemOrderModule(itemModule, 1));
        }
        setupRecyclerItemsOrder(itemsOrder);

        numberOfGoods.setText("Tiền hàng ("+itemsOrder.size()+")");
        money += itemModule.getPriceItem();
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        moneyOnGoods.setText(formatter.format(money)+"đ");
        txtTotal.setText(formatter.format(money)+"đ");
        setupRecyclerItemsOrder(itemsOrder);
    }

    @Override
    public void decreaseItem(ItemOrderModule itemOrderModule) {
        int position = itemsOrder.indexOf(itemOrderModule);
        if (itemsOrder.get(position).getAmountItem() <= 1){
            itemsOrder.remove(itemOrderModule);
        }
        else {
            itemsOrder.set(position, new ItemOrderModule(itemOrderModule.getItemModule(), itemOrderModule.getAmountItem() -1));
        }
        numberOfGoods.setText("Tiền hàng ("+itemsOrder.size()+")");
        money -= itemOrderModule.getItemModule().getPriceItem();
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        moneyOnGoods.setText(formatter.format(money)+"đ");
        txtTotal.setText(formatter.format(money)+"đ");
        setupRecyclerItemsOrder(itemsOrder);

    }
    @Override
    public void onOrder(ItemModule itemModule) {

    }
}
