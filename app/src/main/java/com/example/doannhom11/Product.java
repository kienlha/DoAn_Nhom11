package com.example.doannhom11;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.function.Function;

public class Product {
    private String masp,tensp;
    private int gia;

    public Product() {
    }

    public Product(String masp, String tensp, int gia) {
        this.masp = masp;
        this.tensp = tensp;
        this.gia = gia;
    }
    public String getMasp() {
        return masp;
    }

    public void setMasp(String masp) {
        this.masp = masp;
    }

    public String getTensp() {
        return tensp;
    }

    public void setTensp(String tensp) {
        this.tensp = tensp;
    }

    public int getGia() {
        return gia;
    }

    public void setGia(int gia) {
        this.gia = gia;
    }
}
class ProductAdapter extends BaseAdapter
{
    TextView tvname,tvmasp,tvprice;
    ImageView ava;
    private Context m_Context;
    private ArrayList<Product> m_array,temp_array;
    private int m_Layout;
    public ProductAdapter(Context context, int layout, ArrayList<Product> arrayList)
    {
        m_Context = context;
        m_Layout = layout;
        m_array = arrayList;
        temp_array = arrayList;
    }
    @Override
    public int getCount() {
        return m_array.size();
    }

    @Override
    public Object getItem(int i) {
        return m_array.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(m_Context).inflate(m_Layout,null);
        return view;
    }
}
