package com.example.apptracnghiem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.apptracnghiem.Adapters.QuestionAdapter;
import com.example.apptracnghiem.Models.QuestionModel;
import com.example.apptracnghiem.databinding.ActivityQuestionBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class QuestionActivity extends AppCompatActivity {

    ActivityQuestionBinding binding;
    FirebaseDatabase database;
    ArrayList<QuestionModel>list;
    QuestionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuestionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseDatabase.getInstance();
        list = new ArrayList<>();



        int setNum = getIntent().getIntExtra("setNum", 0);
        String monName = getIntent().getStringExtra("monName");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyQuestion.setLayoutManager(layoutManager);

        adapter = new QuestionAdapter(this, list, monName, new QuestionAdapter.DeleteListener() {
            @Override
            public void onLongClick(int position, String id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(QuestionActivity.this);
                builder.setTitle("Xóa câu hỏi");
                builder.setMessage("Bạn có chắc chắn muốn xóa câu hỏi này");

                builder.setPositiveButton("Yes", (dialogInterface, i) -> {

                    database.getReference().child("Sets").child(monName).child("questions")
                            .child(id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                    Toast.makeText(QuestionActivity.this, "Đã xóa câu hỏi", Toast.LENGTH_SHORT).show();
                                }
                            });
                });

                builder.setNegativeButton("No", (dialogInterface, i) -> {
                    dialogInterface.dismiss();

                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        binding.recyQuestion.setAdapter(adapter);

        database.getReference().child("Sets").child(monName).child("questions")
                        .orderByChild("setNum").equalTo(setNum)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                if(snapshot.exists()){
                                    list.clear();
                                    for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                                        QuestionModel model = dataSnapshot.getValue(QuestionModel.class);
                                        model.setKey(dataSnapshot.getKey());
                                        list.add(model);
                                    }

                                }
                                else {
                                    Toast.makeText(QuestionActivity.this, "Câu hỏi không tồn tại", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

        binding.addQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(QuestionActivity.this, AddQuestionActivity.class);
                intent.putExtra("mon", monName);
                intent.putExtra("setNum", setNum);
                startActivity(intent);
            }
        });
        binding.backQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}