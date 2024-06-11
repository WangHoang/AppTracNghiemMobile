package com.example.apptracnghiem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.apptracnghiem.Adapters.MonAdapter;
import com.example.apptracnghiem.Models.MonModel;
import com.example.apptracnghiem.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseDatabase database;
    FirebaseStorage storage;
    CircleImageView monImage;
    EditText inputMon;
    Button uploadMon;
    View fetchImage;
    Dialog dialog;
    Uri imageUri;
    int i = 0;
    ArrayList<MonModel>list;
    MonAdapter adapter;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //getSupportActionBar().hide();

        database = FirebaseDatabase.getInstance();
        storage =FirebaseStorage.getInstance();

        list= new ArrayList<>();

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.item_add_mon);
        if(dialog.getWindow()!= null){
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(true);
        }
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Đang thêm");
        progressDialog.setMessage("Vui lòng đợi");
        uploadMon = dialog.findViewById(R.id.btnUpload);
        inputMon = dialog.findViewById(R.id.inputMon);
        monImage = dialog.findViewById(R.id.monImage);
        fetchImage = dialog.findViewById(R.id.fetchImage);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        binding.recyMon.setLayoutManager(layoutManager);
        adapter = new MonAdapter(this, list);
        binding.recyMon.setAdapter(adapter);

        database.getReference().child("cac mon").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    list.clear();
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        list.add(new MonModel(

                                dataSnapshot.child("monName").getValue().toString(),
                                dataSnapshot.child("monImage").getValue().toString(),
                                dataSnapshot.getKey(),
                                Integer.parseInt(dataSnapshot.child("setNum").getValue().toString())
                        ));
                    }
                    adapter.notifyDataSetChanged();

                }
                else {
                    Toast.makeText(MainActivity.this, "Môn không tồn tại", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


        binding.addMon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

        fetchImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });
        uploadMon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = inputMon.getText().toString();
                if(imageUri ==null){
                    Toast.makeText(MainActivity.this, "Vui lòng cập nhập ảnh môn học", Toast.LENGTH_SHORT).show();
                }
                else if(name.isEmpty()){
                    inputMon.setError("Nhập tên môn học");
                }
                else {
                    progressDialog.show();
                    uploadData();
                }
            }
        });

    }

    private void uploadData() {

        final StorageReference reference = storage.getReference().child("mon").child(new Date().getTime()+"");
        reference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        MonModel monModel = new MonModel();
                        monModel.setMonName(inputMon.getText().toString());
                        monModel.setSetNum(0);
                        monModel.setMonImage(uri.toString());

                        database.getReference().child("cac mon").push().setValue(monModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(MainActivity.this, "Đã thêm", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                progressDialog.dismiss();
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1){
            if(data!=null){
                imageUri = data.getData();
                monImage.setImageURI(imageUri);
            }
        }
    }
}