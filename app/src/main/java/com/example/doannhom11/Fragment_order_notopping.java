package com.example.doannhom11;


import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_order_notopping#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_order_notopping extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment_order_notopping() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_order_notopping.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_order_notopping newInstance(String param1, String param2) {
        Fragment_order_notopping fragment = new Fragment_order_notopping();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    TextView name,soluong,gia,m,l;
    Button btnthemngay;
    Boolean bl,bm;
    ImageView add,remove, image;
    int sl;
    Bundle bund;
    String size = "M", theloai, masp, tableId;
    final Calendar instance = Calendar.getInstance();
    FirebaseAuth mAuth;
    DocumentReference db;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_order_notopping, container, false);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance().document("CUAHANG/" + mAuth.getUid());

        name = (TextView) v.findViewById(R.id.tvOrdernotopping);
        soluong = (TextView) v.findViewById(R.id.tvQuantitynotopping);

        gia = (TextView) v.findViewById(R.id.tvPricenotopping);

        m = (TextView) v.findViewById(R.id.sizeMnotopping);
        l = (TextView) v.findViewById(R.id.sizeLnotopping);

        bund = getArguments(); // lấy giá trị, có số bàn
        tableId = bund.getString("soban");
        masp = bund.getString("MASP");
        theloai = bund.getString("theloai");

        add = (ImageView) v.findViewById(R.id.btnAddnotopping);
        remove = (ImageView) v.findViewById(R.id.btnRemovenotopping);
        image = (ImageView) v.findViewById(R.id.imgOdernotopping);

        sl = 1;

        bm = true;
        bl = false;

        ImageLoader.Load("images/goods/" + masp + ".jpg", image);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sl++;
                soluong.setText(String.valueOf(sl));
                if (bm)
                    gia.setText(String.valueOf(sl*( bund.getInt("GIA")   ) ));
                else
                    gia.setText(String.valueOf(sl*( bund.getInt("GIA")   + 5000 ) ));
            }
        });
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sl>1)
                {
                    sl--;
                    soluong.setText(String.valueOf(sl));

                    if (bm)
                        gia.setText(String.valueOf(sl*( bund.getInt("GIA")   ) ));
                    else
                        gia.setText(String.valueOf(sl*( bund.getInt("GIA")  + 5000 ) ));
                }
            }
        });

//

        m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( bl )
                {
                    size = "M";
                    gia.setText(String.valueOf(sl*( bund.getInt("GIA")   ) ));
                    bm = true;
                    m.setTextColor(Color.parseColor("#ffffff"));
                    m.setBackgroundResource(R.drawable.round_bg);

                    bl = false;
                    l.setText("L");
                    l.setTextColor(Color.parseColor("#000000"));
                    l.setBackgroundResource(R.drawable.round_bg_white);

                }
            }
        });

        l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( bm )
                {
                    size = "L";
                    gia.setText(String.valueOf(sl*( bund.getInt("GIA") + 5000  ) ));
                    bl = true;
                    l.setTextColor(Color.parseColor("#ffffff"));
                    l.setBackgroundResource(R.drawable.round_bg);

                    bm = false;
                    m.setText("M");
                    m.setTextColor(Color.parseColor("#111111"));
                    m.setBackgroundResource(R.drawable.round_bg_white);

                }
            }
        });

        name.setText(bund.getString("TENSP"));
        gia.setText(String.valueOf(bund.getInt("GIA")));

        btnthemngay = (Button) v.findViewById(R.id.btnOrderNownotopping);
        btnthemngay.setOnClickListener(new View.OnClickListener() { // tên(size), số lượng ,topping, số bàn
            @Override
            public void onClick(View view) {
                saveFoodOrderIntoAFile();

                getActivity().onBackPressed();
            }
        });

        // Xử lý nút back
        ((ImageView)v.findViewById(R.id.backno)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        return v;
    }

    private void saveFoodOrderIntoAFile() {
        Map<String, Object> map = new HashMap<>();
        map.put("sp_ref_name", db.collection(theloai).document(masp));
        map.put("SIZE", size);
        map.put("SOLUONG", Long.parseLong(soluong.getText().toString()));
        map.put("DONE", false);
        map.put("GIA", Long.parseLong(gia.getText().toString()));


        db.collection("/TableStatus/" + tableId + "/DrinksOrder").add(map)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        Map<String, Object> queue = new HashMap<>();
                        queue.put("food_name", db.collection("/TableStatus/" + tableId + "/DrinksOrder/").document(task.getResult().getId()));
                        queue.put("TIME", instance.getTimeInMillis() / 1000);
                        db.collection("/FoodQueue").add(queue);
                    }
                });
    }


}