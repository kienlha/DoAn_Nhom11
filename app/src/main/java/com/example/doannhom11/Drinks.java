package com.example.doannhom11;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public class Drinks {
    private String name,size,topping;
    private int ban;

    public Drinks(String name, String size, String topping, int ban) {
        this.name = name;
        this.size = size;
        this.topping = topping;
        this.ban = ban;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSize() {
        return size;
    }
    public void setSize(String size) {
        this.size = size;
    }
    public String getTopping() {
        return topping;
    }
    public void setTopping(String topping) {
        this.topping = topping;
    }
    public int getBan() {
        return ban;
    }
    public void setBan(int ban) {
        this.ban = ban;
    }
}
class DrinksAdapter extends BaseAdapter
{
    private Context m_Context;
    private ArrayList<Drinks> m_array;
    private int m_Layout;
    public DrinksAdapter (Context m_Context,int m_Layout, ArrayList<Drinks> m_array)
    {
        this.m_Context=m_Context;
        this.m_Layout=m_Layout;
        this.m_array=m_array;
    }
    @Override
    public int getCount() {
        return m_array.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(m_Context).inflate(m_Layout,null);
        return null;
    }
}
