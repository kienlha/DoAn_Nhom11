package com.example.doannhom11;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderDrinks {
    private String name,size, id;
    private ArrayList<Product> topping;
    private int soban, soluong;
    long gia;


    public OrderDrinks(String id)
    {
        this.id = id;
    }
    public OrderDrinks(String id, String name, String size, int soluong, int soban, long gia) {
        this.id = id;
        this.name = name;
        this.size = size;
        this.soluong = soluong;
        this.soban = soban;
        this.gia = gia;
    }
    public OrderDrinks(String id, String name, String size, int soluong, ArrayList<Product> topping, int soban, long gia) {
        this.id = id;
        this.name = name;
        this.size = size;
        this.soluong = soluong;
        this.topping = topping;
        this.soban = soban;
        this.gia = gia;
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

    public int getSoluong() {
        return soluong;
    }

    public void setSoluong(int soluong) {
        this.soluong = soluong;
    }

    public int getSoban() {
        return soban;
    }

    public void setSoban(int soban) {
        this.soban = soban;
    }


    public ArrayList<Product> getTopping() {
        return topping;
    }

    public void addTopping(Product topping){
        this.topping.add(topping);
    }

    public void setTopping(ArrayList<Product> topping) {
        this.topping = topping;
    }

    public String getId() {
        return id;
    }

    public long getGia() {
        return gia;
    }

    public void setGia(long gia) {
        this.gia = gia;
    }
}

class  OrderDrinksAdapter extends BaseAdapter {
    TextView tvnametable,tvsoluong,tvtopping,tvtable;
    ImageView daubacham;
    MenuBuilder menuBuilder;
    FirebaseAuth mAuth;
    DocumentReference db;

    private Context m_Context;
    private ArrayList<OrderDrinks> m_array;
    private int m_Layout;
    public OrderDrinksAdapter(Context context, int layout, ArrayList<OrderDrinks> arrayList) {
        m_Context = context;
        m_Layout = layout;
        m_array = arrayList;
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
        return m_array.get(i).getSoban();
    }

    @SuppressLint("RestrictedApi")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) { // tên(size), số lượng ,topping, số bàn
        view = LayoutInflater.from(m_Context).inflate(m_Layout,null);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance().document("CUAHANG/" + mAuth.getUid());

        tvnametable = (TextView) view.findViewById(R.id.tvnametable);
        tvsoluong = (TextView) view.findViewById(R.id.tvsoluong);
        tvtopping = (TextView) view.findViewById(R.id.tvtopping);
        tvtable = (TextView) view.findViewById(R.id.tvtable);
        daubacham = (ImageView)view.findViewById(R.id.imgDetail);

        tvnametable.setText(m_array.get(i).getName());

        ImageView image = (ImageView) view.findViewById(R.id.imageDrink);

        db.collection("FoodQueue").document(m_array.get(i).getId()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        task.getResult().getDocumentReference("food_name").get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task1) {
                                        task1.getResult().getDocumentReference("sp_ref_name").get()
                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task2) {
                                                        ImageLoader.Load( "/images/goods/"  + task2.getResult().getId() + ".jpg", image);

                                                    }
                                                });
                                    }
                                });
                    }
                });

        tvsoluong.setText("Số lượng: "+ m_array.get(i).getSoluong() +"(" +  m_array.get(i).getSize() + ")");

        if (m_array.get(i).getTopping()==null || m_array.get(i).getTopping().equals(" ")) // ẩn đi textview topping nếu ko phải là trà sữa
            tvtopping.setVisibility(View.INVISIBLE);
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

            tvtopping.setText( "Topping: " + tp);
        }

        tvtable.setText("Bàn: " + m_array.get(i).getSoban());

        daubacham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuBuilder = new MenuBuilder(view.getContext());
                MenuInflater inflater = new MenuInflater(view.getContext());

                inflater.inflate(R.menu.menu_for_orderdrinks, menuBuilder);

                MenuPopupHelper menuPopupHelper = new MenuPopupHelper(view.getContext(), menuBuilder, view);
                menuPopupHelper.setForceShowIcon(true);

                menuBuilder.setCallback(new MenuBuilder.Callback() {
                    @Override
                    public boolean onMenuItemSelected(@NonNull MenuBuilder menu, @NonNull MenuItem item) {
                        if (item.getTitle().equals("Hoàn thành")) {
                            DocumentReference ref = db.collection("FoodQueue").document(m_array.get(i).getId());
                            Task<DocumentSnapshot> task = ref.get();
                            while(!task.isComplete());

                            Map<String, Object> map = new HashMap<>();
                            map.put("DONE", true);

                            task.getResult().getDocumentReference("food_name").update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                }
                            });

                            task.getResult().getReference().delete();
                            m_array.remove(i);
                            notifyDataSetChanged();

                        }
                        else if (item.getTitle().equals("Hủy bỏ")) {
                            DocumentReference ref = db.collection("FoodQueue").document(m_array.get(i).getId());
                            Task<DocumentSnapshot> task1 = ref.get();
                            while(!task1.isComplete());
                            task1.getResult().getDocumentReference("food_name").collection("Topping").get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task2) {
                                            for(DocumentSnapshot dataa : task2.getResult()){
                                                dataa.getReference().delete();
                                            }
                                            task1.getResult().getDocumentReference("food_name").delete();
                                            ref.delete();
                                        }
                                    });
                            notifyDataSetChanged();
                        }
                        return true;
                    }

                    @Override
                    public void onMenuModeChange(@NonNull MenuBuilder menu) {

                    }
                });
                menuPopupHelper.show();
            }
        });
        return view;
    }
}

