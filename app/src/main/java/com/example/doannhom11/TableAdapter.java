package com.example.doannhom11;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

class MyBool
{
    private boolean value;
    private int index;
    private String id;

    public MyBool(String id , boolean value, int index)
    {
        this.id = id;
        this.value = value;
        this.index = index;
    }

    public boolean Get() {return this.value;}
    public void Set(boolean value) {this.value = value;}
    public int getIndex() {
        return index;
    }
    public void setIndex(int index) {
        this.index = index;
    }
    public String getId() {
        return id;
    }
}

public class TableAdapter extends BaseAdapter {
    private ArrayList<MyBool> table;
    private Context context;

    public TableAdapter() {
    }

    public TableAdapter(Context context, ArrayList<MyBool> table) {
        this.context = context;
        this.table = table;
    }

    @Override
    public int getCount() {
        return table.size();
    }

    @Override
    public Object getItem(int i) {
        return table.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(table.get(i).Get()){
            view = LayoutInflater.from(context).inflate(R.layout.layout_table_occupied, viewGroup, false);
            ((TextView)view.findViewById(R.id.title1)).setText("Bàn " + table.get(i).getIndex());
        }
        else {
            view = LayoutInflater.from(context).inflate(R.layout.layout_table_empty, viewGroup, false);
            ((TextView)view.findViewById(R.id.title2)).setText("Bàn " + table.get(i).getIndex());
        }
        return view;
    }

}
