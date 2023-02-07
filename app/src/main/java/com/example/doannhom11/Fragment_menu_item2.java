package com.example.doannhom11;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_menu_item2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_menu_item2 extends Fragment implements TextWatcher {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment_menu_item2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_menu_item2.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_menu_item2 newInstance(String param1, String param2) {
        Fragment_menu_item2 fragment = new Fragment_menu_item2();
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

    ProductAdapterUpdate adapter;
    ArrayList<Product> arrayList;
    ListView lvcoffeeno;
    EditText edtcoffeeno;
    String tam;
    FirebaseAuth mAuth;
    DocumentReference db;

    @SuppressLint({"RestrictedApi", "Range"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_menu_item2, container, false);

        edtcoffeeno = (EditText) v.findViewById(R.id.edtcoffeeno);
        lvcoffeeno = (ListView) v.findViewById(R.id.lvcoffeeno);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance().document("CUAHANG/" + mAuth.getUid());

        Bundle bundle = getArguments(); // có cái temp: tức là chọn vào cái nào của menu và số bàn
        tam = bundle.getString("temp");
        String query = "";

        switch (tam)
        {
            case "coffee":
                query = "/SANPHAM/CAPHE/DANHSACHCAPHE";
                break;
            case "trasua":
                query = "/SANPHAM/TRASUA/DANHSACHTRASUA";
                break;
            case "sinhto":
                query = "/SANPHAM/SINHTO/DANHSACHSINHTO";
                break;
            case "topping":
                query = "/SANPHAM/TRANGMIENG/DANHSACHTRANGMIENG";
                break;
            case "topping1":
                query = "/SANPHAM/TOPPING/DANHSACHTOPPING";
                break;
        }

        edtcoffeeno.addTextChangedListener(this);
        db.collection(query).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful())
                {
                    arrayList = new ArrayList<>();
                    adapter = new ProductAdapterUpdate(getActivity(),R.layout.layout_menu_item_notable2,arrayList);
                    lvcoffeeno.setAdapter(adapter);

                    for (QueryDocumentSnapshot data : task.getResult())
                    {
                        String MASP = data.getId(),
                                TENSP = data.getString("TEN");
                        Long GIA = (Long)data.get("GIA");

                        arrayList.add(new Product(MASP, TENSP, GIA.intValue()));
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });

        ((ImageView)v.findViewById(R.id.btnAddDrink)).setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.action_fragment_menu_item2_to_fragment_item_edit);
        });

        lvcoffeeno.setOnItemClickListener((adapterView, view, i, l) -> {

            bundle.putString("Masp", ((Product)adapterView.getAdapter().getItem(i)).getMasp());
            Navigation.findNavController(view).navigate(R.id.action_fragment_menu_item2_to_fragment_item_info, bundle);
        });

        ((ImageView)v.findViewById(R.id.btnAddDrink)).setOnClickListener(view -> {
            bundle.putString("Masp", null);
            Navigation.findNavController(view).navigate(R.id.action_fragment_menu_item2_to_fragment_item_edit, bundle);
        });

        // Xử lý nút back
        ((ImageView)v.findViewById(R.id.backno)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_fragment_menu_item2_to_fragment_menu);
            }
        });

        return v;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        this.adapter.getFilter().filter(s);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}