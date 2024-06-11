package com.example.apptracnghiem.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apptracnghiem.Models.MonModel;
import com.example.apptracnghiem.R;
import com.example.apptracnghiem.SetsActivity;
import com.example.apptracnghiem.databinding.ItemMonhocBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MonAdapter extends RecyclerView.Adapter<MonAdapter.viewHolder>{

    Context context;
    ArrayList<MonModel>list;

    public MonAdapter(Context context, ArrayList<MonModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_monhoc, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        MonModel model = list.get(position);
        holder.binding.monName.setText(model.getMonName());
        Picasso.get()
                .load(model.getMonImage())
                .placeholder(R.drawable.math)
                .into(holder.binding.monhocImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SetsActivity.class);
                intent.putExtra("mon", model.getMonName());
                intent.putExtra("sets", model.getSetNum());
                intent.putExtra("key", model.getKey());

                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        ItemMonhocBinding binding;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemMonhocBinding.bind(itemView);
        }
    }
}
