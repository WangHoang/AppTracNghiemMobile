package com.example.apptracnghiem.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apptracnghiem.QuestionActivity;
import com.example.apptracnghiem.R;

public class GrideAdapter extends BaseAdapter {
    public int sets = 0;
    private String mon;
    private  String key;
    private GridListener listener;

    public GrideAdapter(int sets, String mon, String key, GridListener gridListener) {
        this.sets = sets;
        this.mon = mon;
        this.key = key;
        this.listener = gridListener;
    }

    @Override
    public int getCount() {
        return sets+1;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View view1;
        if(view==null){
            view1 = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_sets, viewGroup, false);
        }
        else {
            view1=view;
        }
        if(i==0){
            ((TextView)view1.findViewById(R.id.setName)).setText("+");
        }
        else {
            ((TextView)view1.findViewById(R.id.setName)).setText(String.valueOf(i));
        }
        view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(i==0){
                    listener.addSets();
                }
                else {
                    Intent intent = new Intent(viewGroup.getContext(), QuestionActivity.class);
                    intent.putExtra("setNum", i);
                    intent.putExtra("monName", mon);
                    viewGroup.getContext().startActivity(intent);
                }
            }
        });
        return view1;
    }
    public interface GridListener{
        public void addSets();
    }
}
