package com.example.doannhom11;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Bill_adapter extends BaseAdapter {

    TextView drinkName, gia, soluong, toppingList;
    FirebaseFirestore db;
    private Context m_Context;
    private ArrayList<OrderDrinks> m_array;
    private int m_Layout;

    public Bill_adapter(Context m_context, int m_layout, ArrayList<OrderDrinks> m_array) {
        m_Context = m_context;
        this.m_array = m_array;
        m_Layout = m_layout;
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

        db = FirebaseFirestore.getInstance();
        drinkName = view.findViewById(R.id.drinks_name);
        gia = view.findViewById(R.id.price);
        soluong = view.findViewById(R.id.quantity);
        toppingList = view.findViewById(R.id.toppingList);

        drinkName.setText(m_array.get(i).getName());

        gia.setText(m_array.get(i).getGia() + " đ");
        soluong.setText(m_array.get(i).getSoluong() + "(" + m_array.get(i).getSize() + ")");

        if (m_array.get(i).getTopping()==null || m_array.get(i).getTopping().equals(" "))
            toppingList.setVisibility(View.INVISIBLE);
        else {
            String tp = ""; int i1 = 1, max =  m_array.get(i).getTopping().size();
            for (Product data : m_array.get(i).getTopping()){
                if(i1 >= max){
                    tp += data.getTensp();
                }
                else {
                    tp += data.getTensp() + ", ";
                }
                i1++;
            }
            toppingList.setText( "Topping: " + tp);
        }

        return view;
    }


}

