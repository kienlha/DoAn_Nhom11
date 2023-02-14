package com.example.doannhom11;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * Use the {@link Fragment_staff#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_staff extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment_staff() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_staff.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_staff newInstance(String param1, String param2) {
        Fragment_staff fragment = new Fragment_staff();
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
    ImageView btnAddStaff;
    ArrayList<Staff> staffs;
    StaffAdapter adapter;
    ListView listView ;
    FirebaseAuth mAuth;
    DocumentReference db;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_staff, container, false);

        listView = (ListView) root.findViewById(R.id.ListOfStaffs);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance().document("CUAHANG/" + mAuth.getUid());

        db.collection("/NHANVIEN").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful())
                {
                    staffs = new ArrayList<>();
                    for (QueryDocumentSnapshot data : task.getResult())
                    {
                        String CCCD = data.getString("CCCD"),
                                HOTEN = data.getString("HOTEN"),
                                NGAYSINH = data.getString("NGAYSINH"),
                                GIOITINH = data.getString("GIOITINH"),
                                SDT = data.getString("SDT"),
                                NGVL = data.getString("NGVL"),
                                CHUCVU = data.getString("CHUCVU"),
                                MANV = data.getId(),
                                Email = data.getString("EMAIL");

                        staffs.add(new Staff(MANV, HOTEN, NGAYSINH, GIOITINH ,SDT, NGVL, CHUCVU, CCCD, Email ));
                    }
                    adapter = new StaffAdapter(getActivity(), R.layout.layout_staff_manage, staffs);
                    listView.setAdapter(adapter);

                }
            }
        });


        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            Bundle bundle = new Bundle();
            bundle.putString("MANV", staffs.get(i).MaNhanVien());
            Navigation.findNavController(view).navigate(R.id.action_fragment_staff_to_fragment_staff_info, bundle);
        });

        ((ImageView)root.findViewById(R.id.btnAddInfo)).setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.action_fragment_staff_to_fragment_staff_edit);
        });

        return root;
    }
}
