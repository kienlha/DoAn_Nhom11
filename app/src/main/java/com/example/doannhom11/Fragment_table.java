package com.example.doannhom11;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_table#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_table extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment_table() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_table.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_table newInstance(String param1, String param2) {
        Fragment_table fragment = new Fragment_table();
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

    GridView tableList;
    ArrayList<MyBool> table;
    TableAdapter adapter;
    MenuBuilder menuBuilder;
    DocumentReference db;
    FirebaseAuth mAuth;

    ImageView addImg;
    SwipeRefreshLayout refreshLayout;

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_table, container, false);
        tableList = (GridView) view.findViewById(R.id.tableList);
        addImg = (ImageView)view.findViewById(R.id.btnAddTable);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeLayoutTable);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance().document("CUAHANG/" + mAuth.getUid());

        addImg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.layout_table_add);

                Window window = dialog.getWindow();
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                if(window == null){
                    return;
                }

                ((Button)dialog.findViewById(R.id.btnCancel)).setOnClickListener(view1 ->  {
                    dialog.dismiss();
                });

                ((Button)dialog.findViewById(R.id.btnAdd)).setOnClickListener(view2 -> {
                    String s = ((EditText)dialog.findViewById(R.id.edtTableIndex)).getText().toString();
                    if (!s.isEmpty())
                    {
                        int index = Integer.parseInt(s);
                        db.collection("TableStatus").whereEqualTo("Index", index).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful())
                                {
                                    for (DocumentSnapshot data : task.getResult())
                                    {
                                        CustomToast.e(getActivity(), "Bàn số " + index + " đã có sẵn", Toast.LENGTH_SHORT);
                                        return;
                                    }
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("Index", index);
                                    db.collection("TableStatus").add(map).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentReference> task) {
                                            Map<String, Object> map = new HashMap<>();
                                            map.put("status", false);
                                            db.collection("/TableStatus/").document(task.getResult().getId()).update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    CreateList();
                                                }
                                            });
                                        }
                                    });
                                    dialog.dismiss();
                                }
                                else
                                    CustomToast.e(getActivity(), task.getException().getMessage(), Toast.LENGTH_LONG);
                            }
                        });
                    }
                    else
                        CustomToast.e(getActivity(), "Vui lòng nhập số bàn!", Toast.LENGTH_SHORT);
                });

                dialog.show();

                CreateList();
            }
        });



        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                CreateList();
                refreshLayout.setRefreshing(false);
            }
        });

        CreateList();

        return view;
    }

    private void CreateList() {

        db.collection("/TableStatus").orderBy("Index", Query.Direction.ASCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    table = new ArrayList<>();
                    adapter = new TableAdapter(getContext(), table);
                    tableList.setAdapter(adapter);
                    for (QueryDocumentSnapshot data : task.getResult()) {
                        MyBool val = new MyBool(data.getId(), false, Integer.parseInt(data.getLong("Index")+""));
                        table.add(val);

                        db.collection("TableStatus/" + data.getId() + "/DrinksOrder").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task1) {
                                for (DocumentSnapshot data : task1.getResult())
                                {
                                    val.Set(true);
                                    adapter.notifyDataSetChanged();
                                    break;
                                }

                            }
                        });
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });

    }
}