package com.example.fnbapp.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fnbapp.Adapter.RestaurantSettingAdapter;
import com.example.fnbapp.Fragment.restaurantString.AccountFragment;
import com.example.fnbapp.Fragment.restaurantString.PaymentMethodsFragment;
import com.example.fnbapp.Fragment.restaurantString.PrinterSettingFragment;
import com.example.fnbapp.Fragment.restaurantString.ReportSettingFragment;
import com.example.fnbapp.Fragment.restaurantString.SaleSetupFragment;
import com.example.fnbapp.Fragment.restaurantString.StoreInformationFragment;
import com.example.fnbapp.Fragment.restaurantString.TableManagementFragment;
import com.example.fnbapp.Interface.Callback;
import com.example.fnbapp.Module.ItemModule;
import com.example.fnbapp.Module.RestaurantSetting;
import com.example.fnbapp.R;

import java.util.ArrayList;

public class RestaurantSettingFragment extends Fragment implements Callback {
    View view;
    ArrayList<RestaurantSetting> dataSetting;
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.restaurant_setting_fragment, container, false);
        setDataSetup();
        setupRecycler();
        loadFragment(new StoreInformationFragment());
        return  view;
    }
    private void setDataSetup(){
        dataSetting = new ArrayList<>();
        dataSetting.add(new RestaurantSetting(1, getString(R.string.restaurant_information), R.drawable.store));
        dataSetting.add(new RestaurantSetting( 2, getString(R.string.sale_setup), R.drawable.store_setting));
        dataSetting.add(new RestaurantSetting( 3, getString(R.string.report_setting), R.drawable.report));
        dataSetting.add(new RestaurantSetting( 4, getString(R.string.printer), R.drawable.print));
        dataSetting.add(new RestaurantSetting( 5, getString(R.string.payment_methods), R.drawable.card_payment));
        dataSetting.add(new RestaurantSetting( 6, getString(R.string.account), R.drawable.user_shield));
        dataSetting.add(new RestaurantSetting( 7, getString(R.string.table_management), R.drawable.restaurant_table));
        dataSetting.add(new RestaurantSetting( 8, getString(R.string.kitchen_management), R.drawable.waiter));
        dataSetting.add(new RestaurantSetting( 9, getString(R.string.shipment), R.drawable.supplier));
    }
    private  void setupRecycler(){
        recyclerView = view.findViewById(R.id.recyclerRestaurantSetting);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        RestaurantSettingAdapter adapter = new RestaurantSettingAdapter(getContext(), dataSetting, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onChooseStoreSetting(int id) {
        Fragment fragment = null;
        switch (id){
            case 1:
                fragment = new StoreInformationFragment();
                loadFragment(fragment);
                break;
            case 2:
                fragment = new SaleSetupFragment();
                loadFragment(fragment);
                break;
            case 3:
                fragment = new ReportSettingFragment();
                loadFragment(fragment);
                break;
            case 4:
                fragment = new PrinterSettingFragment();
                loadFragment(fragment);
                break;
            case 5:
                fragment = new PaymentMethodsFragment();
                loadFragment( fragment);
                break;
            case 6:
                fragment = new AccountFragment();
                loadFragment(fragment);
                break;
            case 7:
                fragment = new TableManagementFragment();
                loadFragment(fragment);
                break;


        }
    }



    private  void loadFragment(Fragment fragment){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frameRestaurantSetting, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
