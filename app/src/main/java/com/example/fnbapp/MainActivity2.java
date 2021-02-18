package com.example.fnbapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.fnbapp.Fragment.BillFragment;
import com.example.fnbapp.Fragment.HomeFragment;
import com.example.fnbapp.Fragment.InventoryFragment;
import com.example.fnbapp.Fragment.ManagementFragment;
import com.example.fnbapp.Fragment.ProduceFragment;
import com.example.fnbapp.Fragment.RestaurantSettingFragment;
import com.example.fnbapp.Fragment.RevenueExpenditureFragment;
import com.example.fnbapp.Fragment.ShiftManagementFragment;
import com.google.android.material.navigation.NavigationView;

public class MainActivity2 extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
Toolbar toolbar;
ActionBarDrawerToggle toggle ;
DrawerLayout drawerLayout;
NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        innit();
    }
    private  void  innit(){

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);



        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        loadFragment(new HomeFragment());
        navigationView.setNavigationItemSelectedListener(this);
    }
    public void loadFragment(Fragment fragment) {
        Log.e("TAG", "kk");
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

        drawerLayout.closeDrawers();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return  true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item))
        {
            return  true;
        }
        switch (item.getItemId()){
            case android.R.id.home:
                drawerLayout.isDrawerOpen(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment= null;
        switch (menuItem.getItemId()){
            case  R.id.navHome:
                fragment = new HomeFragment();
                loadFragment(fragment);
                break;
            case R.id.navBill:
                fragment = new BillFragment();
                loadFragment(fragment);
                break;
            case R.id.navItem:
                fragment = new ProduceFragment();
                loadFragment(fragment);
                break;
            case R.id.navInventory:
                fragment = new InventoryFragment();
                loadFragment(fragment);
                break;
            case R.id.navRestaurantSetting:
                fragment = new RestaurantSettingFragment();
                loadFragment(fragment);
                break;
            case R.id.navRevenueExpenditure:
                fragment = new RevenueExpenditureFragment();
                loadFragment(fragment);
                break;
            case R.id.navShiftManagement:
                fragment = new ShiftManagementFragment();
                loadFragment(fragment);
                break;
            case R.id.navManagement:
                fragment = new ManagementFragment();
                loadFragment(fragment);
                break;

        }
        return false;
    }
}