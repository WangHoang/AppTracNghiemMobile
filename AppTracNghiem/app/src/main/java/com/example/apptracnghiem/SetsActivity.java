package com.example.apptracnghiem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.apptracnghiem.Adapters.GrideAdapter;
import com.example.apptracnghiem.databinding.ActivitySetsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

public class SetsActivity extends AppCompatActivity {
    ActivitySetsBinding binding;
    FirebaseDatabase database;
    
    GrideAdapter adapter;
    int a = 1;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySetsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        //getSupportActionBar().hide();
        
        database = FirebaseDatabase.getInstance();
        key = getIntent().getStringExtra("key");
        
        adapter = new GrideAdapter(getIntent().getIntExtra("sets", 0),
                getIntent().getStringExtra("mon"), key, new GrideAdapter.GridListener() {
            @Override
            public void addSets() {
                database.getReference().child("cac mon").child(key)
                        .child("setNum").setValue(getIntent().getIntExtra("sets", 0)+a++).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    adapter.sets++;
                                    adapter.notifyDataSetChanged();
                                }
                                else {
                                    Toast.makeText(SetsActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

                binding.gridView.setAdapter(adapter);
        binding.backSets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
