package com.example.fnbapp.Fragment.produceSetting;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fnbapp.Adapter.ItemsAdapter;
import com.example.fnbapp.Fragment.ProduceFragment;
import com.example.fnbapp.Interface.CallBackItems;
import com.example.fnbapp.Interface.Callback;
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
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class ItemListFragment extends Fragment implements CallBackItems {
    private static final int PERMISSION_CODE = 123;
    private static final int REQUEST_CODE_IMAGE_CAPTURE = 456 ;
    private static final int REQUEST_CODE_PICK_CAPTURE = 789;
    View view;
    private RecyclerView recyclerView;
    ItemsAdapter adapter;
    DatabaseReference reference ;
    List<ItemModule> itemModuleList;
    List<ItemModule> itemChoose;
    CheckBox checkBoxAll;
    Button btnDeleteItem ;
    Uri imageUri, uriDownload;
    int valueDelete;
    private ImageView imageItem;
    private EditText edtItemName, edtPriceItem, edtDescription;
    private RadioButton rdbInventoryManagement, rdbRawMaterialManagement;
    boolean isWeight = false;
    boolean isWarehouseLink = false;
    private String key;
    private ProgressDialog progressDialog;
    private ArrayList<String> unitList, categoryList, menuList;
    private Spinner spinnerUnit, spinnerCategory, spinnerMenu;
    protected String valueUnit, valueCategory, valueMenu, strItemName, strPriceItem, strDescription, strWarehouseLink = "";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.item_list_fragment, container, false);
        init();

        getDataFromBD();
        return  view;
    }
    private void init(){
         checkBoxAll = view.findViewById(R.id.checkBoxAllItem);
         itemChoose = new ArrayList<>();
         btnDeleteItem = view.findViewById(R.id.btnDeleteItem);
         btnDeleteItem.setEnabled(false);

    }

    @Override
    public void onResume() {
        super.onResume();
getDataFromBD();
        checkAllItem();
        deleteItem();
        unitList = new ArrayList<>();
        categoryList = new ArrayList<>();
        progressDialog = new ProgressDialog(getContext());
    }

    private void getDataFromBD(){

        reference = FirebaseDatabase.getInstance("https://fnb-foods-default-rtdb.firebaseio.com/")
                .getReference("items");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    itemModuleList = new ArrayList<>();
                    ItemModule itemModule ;
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        itemModule = dataSnapshot.getValue(ItemModule.class);
                        itemModule.setKey(dataSnapshot.getKey());
                        itemModuleList.add(itemModule);
                    }
                    setRecyclerView(itemModuleList, false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void setRecyclerView(List<ItemModule> itemModules, Boolean checkAll){
        recyclerView = view.findViewById(R.id.recyclerViewItems);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
        adapter = new ItemsAdapter(view.getContext(), itemModuleList,this, checkAll);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onEditItem(ItemModule itemModule) {
        getDataCategory();
        getDataMenu();
        fakeDataUnit();
        key = itemModule.getKey();
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
        edtItemName.setText(itemModule.getItemName());
        edtPriceItem.setText(String.valueOf(itemModule.getPriceItem()));
        edtDescription.setText(itemModule.getDescription());
        Picasso.get().load(itemModule.getImageItem()).into(imageItem);
        switchWarehouseLink.setChecked(itemModule.getWarehouseLink());
        switchWeight.setChecked(itemModule.getWeight());

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
    CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked){
                strWarehouseLink = buttonView.getText().toString().trim();
            }
        }
    };
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
        progressDialog = new ProgressDialog(view.getContext());
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
        Map<String , Object> map  = new HashMap<>();
        map.put(key, itemModule);
        reference = FirebaseDatabase.getInstance("https://fnb-foods-default-rtdb.firebaseio.com/")
                .getReference("items");
        reference.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(view.getContext(), "Cập nhật thành công", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
                else {
                    Toast.makeText(view.getContext(), "Cập nhật thất bại", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }

            }
        });

    }
    @Override
    public void onChooseItem(ItemModule itemModule) {
        itemChoose.add(itemModule);
        btnDeleteItem.setEnabled(true);

    }

    @Override
    public void onUnChooseItem(ItemModule itemModule) {
        itemChoose.remove(itemModule);
        Log.e("Check", itemChoose.size()+"");
        if (itemChoose.size() == 0){
            btnDeleteItem.setEnabled(false);
        }
    }

    private void  checkAllItem(){
        checkBoxAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                   setRecyclerView(itemModuleList, isChecked);
                    btnDeleteItem.setEnabled(isChecked);
                }
                else {
                    setRecyclerView(itemModuleList, false);

                }
            }
        });
    }
    private void deleteItem(){
        reference = FirebaseDatabase.getInstance("https://fnb-foods-default-rtdb.firebaseio.com/")
                .getReference("items");

        btnDeleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Xóa mặt hàng");
                builder.setMessage("Bạn có chắc chắn xóa "+ itemChoose.size()+" mặt hàng đã chọn");
                builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        valueDelete = 0;
                        for (ItemModule itemModule : itemChoose){
                            reference.child(itemModule.getKey()).removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                valueDelete ++;
                                            }
                                            else {
                                                Log.e("DELETE ITEMS::", task.getException().getMessage());
                                            }
                                        }
                                    });
                        }
                        if (valueDelete> 0){
                            Toast.makeText(view.getContext(), "Xóa Thành công", Toast.LENGTH_LONG).show();
                            getDataFromBD();
                        }
                        else {
                            Toast.makeText(view.getContext(), "Xóa Thất bại", Toast.LENGTH_LONG).show();

                        }
                    }
                });
                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });
    }
    private void getDataCategory(){
        categoryList = new ArrayList<>();
        reference = FirebaseDatabase.getInstance("https://fnb-foods-default-rtdb.firebaseio.com/")
                .getReference("Category");
        reference.addValueEventListener(new ValueEventListener() {
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

    }
    private void getDataMenu(){
        menuList = new ArrayList<>();reference = FirebaseDatabase.getInstance("https://fnb-foods-default-rtdb.firebaseio.com/")
                .getReference("Menu");

        reference.addValueEventListener(new ValueEventListener() {
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

    }
}
