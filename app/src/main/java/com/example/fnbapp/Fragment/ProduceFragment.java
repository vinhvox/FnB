package com.example.fnbapp.Fragment;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.fnbapp.Dialog.DialogUntils;
import com.example.fnbapp.Fragment.produceSetting.CategoryFragment;
import com.example.fnbapp.Fragment.produceSetting.ItemListFragment;
import com.example.fnbapp.Fragment.produceSetting.MenuFragment;
import com.example.fnbapp.Module.ItemModule;
import com.example.fnbapp.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;


public class ProduceFragment extends Fragment {
    View view;
    DialogUntils dialogUntils;
    private String TAG= "ProduceFragment";
    private  int PERMISSION_CODE = 123;
    private  int REQUEST_CODE_IMAGE_CAPTURE = 234;
    private int REQUEST_CODE_PICK_CAPTURE = 345;
    private Uri imageUri, uriDownload;
    private ImageView imageItem;
    private ArrayList<String> unitList, categoryList, menuList;
    private Spinner spinnerUnit, spinnerCategory, spinnerMenu;
    protected String valueUnit, valueCategory, valueMenu, strItemName, strPriceItem, strDescription, strWarehouseLink = "";
    boolean isWeight = false;
    boolean isWarehouseLink = false;
    private  RadioButton rdbInventoryManagement, rdbRawMaterialManagement;
    private ProgressDialog progressDialog;
    private EditText edtItemName, edtPriceItem, edtDescription;
    private LinearLayout fragmentListItem, fragmentCategory, fragmentMenu;
    private int position = 1;
    FirebaseDatabase database;
    DatabaseReference myRef;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.produce_fragment, container, false);
        checkRequestPermission();
        innit();
        fakeDataUnit();
        return  view;
    }
    private void innit(){
        dialogUntils = new DialogUntils();
        database = FirebaseDatabase.getInstance("https://fnb-foods-default-rtdb.firebaseio.com/");
        fragmentListItem = view.findViewById(R.id.fragmentLisItem);
        fragmentCategory = view.findViewById(R.id.fragmentCategory);
        fragmentMenu = view.findViewById(R.id.fragmentMenu);
        Button button = view.findViewById(R.id.btnAddItems);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (position){
                    case 1:
                        openDialogAddItems();
                        break;
                    case 2:
                        openDialogAddCategory();
                        break;
                    case 3:
                        openDialogAddMenu();
                        break;
                }
            }
        });
        progressDialog = new ProgressDialog(getContext());
        loadFragment(new ItemListFragment());
        setFragment();

    }
    public   void openDialogAddItems(){
        getDataCategory();
        getDataMenu();
        Dialog dialog = new Dialog(view.getContext());
        dialog.setContentView(R.layout.dialog_add_items);
        TextView txtEditImageItem = dialog.findViewById(R.id.txtEditImageItem);
         edtItemName = dialog.findViewById(R.id.edtItemName);
        edtPriceItem = dialog.findViewById(R.id.edtPriceItem);
         edtDescription = dialog.findViewById(R.id.edtDescription);
        imageItem = dialog.findViewById(R.id.imageItem);
        spinnerUnit = dialog.findViewById(R.id.spinnerUnit);
        spinnerCategory  = dialog.findViewById(R.id.spinerCategory);
        spinnerMenu = dialog.findViewById(R.id.spinnerMenu);
        Switch switchWeight = dialog.findViewById(R.id.switchWeight);
        Switch switchWarehouseLink = dialog.findViewById(R.id.switchWarehouseLink);
        Button buttonSave = dialog.findViewById(R.id.btnSave);
        rdbInventoryManagement = dialog.findViewById(R.id.rdbInventoryManagement);
         rdbRawMaterialManagement = dialog.findViewById(R.id.rdbRawMaterialManagement);
        switchWarehouseLink.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    isWarehouseLink = true;
                    rdbInventoryManagement.setEnabled(true);
                    rdbRawMaterialManagement.setEnabled(true);
                }
                else {
                    isWarehouseLink = false;
                    rdbInventoryManagement.setEnabled(false);
                    rdbRawMaterialManagement.setEnabled(false);
                }
            }
        });
        switchWeight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    isWeight = true;
                }
                else {
                    isWeight = false;
                }
            }
        });
       rdbInventoryManagement.setOnCheckedChangeListener(listener);
       rdbRawMaterialManagement.setOnCheckedChangeListener(listener);
        setSpinnerUnitValue();
        setSpinnerCategoryValue();
        setSpinnerMenuValue();
        txtEditImageItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenuImages(v);
            }
        });
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onSave();
            }
        });
        dialog.show();
    }
    CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked){
                strWarehouseLink = buttonView.getText().toString().trim();
            }
        }
    };
    private void getDataCategory(){
        categoryList = new ArrayList<>();

        myRef = database.getReference("Category");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        categoryList.add(dataSnapshot.getValue().toString());
                    }
                    setSpinnerCategoryValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Log.e(TAG, "getDataCategory: "+ categoryList.size() );
    }
    private void getDataMenu(){
        menuList = new ArrayList<>();
        myRef = database.getReference("Menu");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        menuList.add(dataSnapshot.getValue().toString());
                    }
                    setSpinnerMenuValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Log.e(TAG, "getDataMenu: "+ menuList.size() );

    }
    private void fakeDataUnit(){
        unitList = new ArrayList<>();
        unitList.add("Đĩa");
        unitList.add("Bát");
        unitList.add("Tô");
        unitList.add("suất");
        unitList.add("Kg");
        unitList.add("Khác");
    }
    private void setSpinnerUnitValue(){
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_1, unitList);
        spinnerUnit.setAdapter(arrayAdapter);
        spinnerUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                valueUnit = unitList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    private void setSpinnerCategoryValue(){
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_1, categoryList);
        spinnerCategory.setAdapter(arrayAdapter);
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                valueCategory = categoryList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    private void setSpinnerMenuValue(){
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_1, menuList);
        spinnerMenu.setAdapter(arrayAdapter);
        spinnerMenu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                valueMenu = menuList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private  void  showMenuImages(View view){
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        popupMenu.inflate(R.menu.menu_image);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menuOpenCamera:
                        openCamera();
                        break;
                    case R.id.menuOpenGallery:
                        openGallery();
                        break;
                }
                return false;
            }
        });
        popupMenu.show();
    }
    private void  checkRequestPermission(){
        if (Build.VERSION.SDK_INT> Build.VERSION_CODES.LOLLIPOP_MR1){
          if (ContextCompat.checkSelfPermission(view.getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED
              || ContextCompat.checkSelfPermission(view.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
              String[] permission ={ Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
              requestPermissions(permission, PERMISSION_CODE);
          }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            }
            else {
                Toast.makeText(view.getContext(), getString(R.string.denied), Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(view.getContext(), getString(R.string.denied), Toast.LENGTH_LONG).show();
        }
    }
    private  void openCamera(){
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, REQUEST_CODE_IMAGE_CAPTURE);
    }
    private void openGallery(){
        Intent intentPickGallery = new Intent(Intent.ACTION_PICK);
        intentPickGallery.setType("image/*");
        startActivityForResult(intentPickGallery, REQUEST_CODE_PICK_CAPTURE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode== REQUEST_CODE_IMAGE_CAPTURE&& resultCode== RESULT_OK&& data!= null){
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageItem.setImageBitmap(imageBitmap);

        }
        if (requestCode== REQUEST_CODE_PICK_CAPTURE&& resultCode== RESULT_OK&& data!= null){
            imageUri = data.getData();
            imageItem.setImageURI(imageUri);
        }
    }
    private void onSave(){

        progressDialog.setMessage(getString(R.string.mess_upload));
        progressDialog.show();
        upLoadImageToStore();

    }
    private void upLoadImageToStore(){
        StorageReference mStorageRef;
        mStorageRef = FirebaseStorage.getInstance("gs://fnb-foods.appspot.com").getReference();
        imageItem.setDrawingCacheEnabled(true);
        imageItem.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imageItem.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        final StorageReference riversRef = mStorageRef.child("images/rivers.jpg");


        final UploadTask uploadTask = riversRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw Objects.requireNonNull(task.getException());
                        }
                        return riversRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            uriDownload = task.getResult();
                            uploadDataToDB();

                        } else {
                            Toast.makeText(getContext(), "Download uri fail",Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();

                        }
                    }
                });
            }
        });
    }
    private  void uploadDataToDB(){
        strItemName = edtItemName.getText().toString().trim();
        strPriceItem = edtPriceItem.getText().toString().trim();
        strDescription = edtDescription.getText().toString().trim();
        ItemModule itemModule = new ItemModule(strItemName, Long.parseLong(strPriceItem), String.valueOf(uriDownload), valueUnit, valueCategory, valueMenu, isWeight, isWarehouseLink, strWarehouseLink, strDescription);

         myRef = database.getReference("items");
        myRef.push().setValue(itemModule).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    progressDialog.dismiss();
                    setEmptyView();

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Lỗi: "+ e.getMessage());
            }
        });
    }
    private void setEmptyView(){
        edtPriceItem.setText("");
        edtDescription.setText("");
        edtItemName.setText("");
    }
    private  void loadFragment(Fragment fragment){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frameProduceSetting, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    private void setFragment(){

        fragmentListItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new ItemListFragment());
                position = 1;
            }
        });
        fragmentCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new CategoryFragment());
                position = 2;
            }
        });
        fragmentMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new MenuFragment());
                position = 3;
            }
        });

    }
    private void openDialogAddCategory(){
      final Dialog  dialog = new Dialog(view.getContext());
        dialog.setContentView(R.layout.dialog_add_category);
        ImageView imageCancel = dialog.findViewById(R.id.imageCancel);
        final EditText edtCategoryName = dialog.findViewById(R.id.edtCategoryName);
        Button btnAddCategory = dialog.findViewById(R.id.btnAddCategory);
        btnAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String categoryName = edtCategoryName.getText().toString().trim();
                if (categoryName.isEmpty()){
                    edtCategoryName.setError(getString(R.string.mess_empty));
                }
                else {
                    saveCategory(dialog,categoryName);
                }
            }
        });
        dialog.show();

    }
    private void openDialogAddMenu(){
        final Dialog  dialog = new Dialog(view.getContext());
        dialog.setContentView(R.layout.dialog_add_menu);
        ImageView imageMenuCancel = dialog.findViewById(R.id.imageMenuCancel);
        final EditText edtMenu = dialog.findViewById(R.id.edtMenu);
        Button btnAddMenu = dialog.findViewById(R.id.btnAddMenu);
        btnAddMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String menu = edtMenu.getText().toString().trim();
                if (menu.isEmpty()){
                    edtMenu.setError(getString(R.string.mess_empty));
                }
                else {
                    saveMenu(dialog,menu);
                }
            }
        });
        dialog.show();
    }
    private void saveCategory(final Dialog dialog, String valueCategory){
        myRef = database.getReference("Category");
        myRef.push().setValue(valueCategory).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    dialog.cancel();
                    Toast.makeText(view.getContext(),"Thêm mới danh mục thành công", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(view.getContext(),"Thêm mới danh mục thất bại", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    private void saveMenu(final Dialog dialog, String valueMenu){
        myRef = database.getReference("Menu");
        myRef.push().setValue(valueMenu).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    dialog.cancel();
                    Toast.makeText(view.getContext(),"Thêm mới thực đơn thành công", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(view.getContext(),"Thêm mới thực đơn thất bại", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
