package com.example.doannhom11;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_staff_edit#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_staff_edit extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment_staff_edit() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_staff_edit.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_staff_edit newInstance(String param1, String param2) {
        Fragment_staff_edit fragment = new Fragment_staff_edit();
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

    ArrayAdapter<CharSequence> genderAdaper;
    ArrayList<String> gender;

    ArrayAdapter<CharSequence> positionAdapter;
    ArrayList<String> positions;

    final Calendar myCalendar1 = Calendar.getInstance(),
            myCalendar2 = Calendar.getInstance();
    EditText edtDob, edtBegin, edtName, edtId,edtPhone, edtHSL;
    ImageView avatar;
    Button btnSave;
    Spinner genderSpinner;
    Spinner positionSpinner;
    FirebaseAuth mAuth;
    DocumentReference db;

    private ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                         if (result.getResultCode() == Activity.RESULT_OK)
                         {
                             Uri data = result.getData().getData();
                             avatar.setImageURI(data);
                         }
                }
            });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_staff_edit, container, false);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance().document("CUAHANG/" + mAuth.getUid());

        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");

        // initialize widget
        edtDob =  (EditText)root.findViewById(R.id.edtDob);
        edtBegin = (EditText)root.findViewById(R.id.edtBeginDate);
        edtName = (EditText)root.findViewById(R.id.edtName);
        edtId = (EditText)root.findViewById(R.id.edtCCCD);
        edtPhone = (EditText)root.findViewById(R.id.edtPhone);
        edtHSL =  (EditText)root.findViewById(R.id.edtHSL);
        avatar = (ImageView) root.findViewById(R.id.avatar);
        btnSave = (Button) root.findViewById(R.id.btnSaveInfoStaff);
        genderSpinner = ((Spinner)root.findViewById(R.id.editGender));
        positionSpinner = ((Spinner)root.findViewById(R.id.editPosition));

        //set spinner
        genderAdaper = ArrayAdapter.createFromResource(getActivity(), R.array.gender, android.R.layout.simple_spinner_item);
        genderAdaper.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(genderAdaper);
        positionAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.position, android.R.layout.simple_spinner_item);
        positionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        positionSpinner.setAdapter(positionAdapter);

        avatar.setOnClickListener(view -> {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            galleryIntent.setType("image/*");
            launcher.launch(galleryIntent);
        });

        btnSave.setOnClickListener(view -> {

            // đợi hoàn thành firestore database
            ImageLoader.Upload("images/staff/", avatar);
            // push du lieu len firestore database
        });

        if (getArguments() != null) // Che do chinh sua nhan vien
        {
            Bundle data = getArguments();

            // hien thi thong tin co the thay doi duoc
            db.collection("/NHANVIEN/").document(data.getString("MANV"))
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful())
                    {
                        DocumentSnapshot snap =  task.getResult();
                        edtName.setText(snap.getString("HOTEN"));
                        edtId.setText(snap.getString("CCCD"));
                        edtDob.setText(snap.getString("NGAYSINH"));
                        edtBegin.setText(snap.getString("NGVL"));
                        edtPhone.setText(snap.getString("SDT"));
                        edtHSL.setText( ""+snap.getString("EMAIL"));

                        int gender;

                        if (snap.getString("GIOITINH").equals("Nam"))
                            gender = 0;
                        else if (snap.getString("GIOITINH").equals("Nữ"))
                            gender = 1;
                        else
                            gender = 2;
                        genderSpinner.setSelection(gender);

                        int position;
                        if (snap.getString("CHUCVU").equals("Quản lý"))
                            position = 0;
                        else if (snap.getString("CHUCVU").equals("Phục vụ"))
                            position = 1;
                        else if (snap.getString("CHUCVU").equals("Thu ngân"))
                            position = 2;
                        else if (snap.getString("CHUCVU").equals("Bảo vệ"))
                            position = 3;
                        else
                            position = 4;
                        positionSpinner.setSelection(position);
                    }
                }
            });

            ImageLoader.Load("images/staff/" + data.getString("MANV") + ".jpg", ((ImageView)root.findViewById(R.id.avatar)));

            try {
                myCalendar1.setTime(dateFormat.parse(edtDob.getText().toString()));
                myCalendar2.setTime(dateFormat.parse(edtBegin.getText().toString()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        edtDob.setOnClickListener(view -> {

            DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    myCalendar1.set(Calendar.YEAR, year);
                    myCalendar1.set(Calendar.MONTH, month);
                    myCalendar1.set(Calendar.DAY_OF_MONTH, day);

                    ((EditText)view).setText(dateFormat.format(myCalendar1.getTime()));
                }
            };
            new DatePickerDialog(getActivity(), date, myCalendar1.get(Calendar.YEAR), myCalendar1.get(Calendar.MONTH), myCalendar1.get(Calendar.DAY_OF_MONTH)).show();
        });

        edtBegin.setOnClickListener(view -> {
            DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    myCalendar2.set(Calendar.YEAR, year);
                    myCalendar2.set(Calendar.MONTH, month);
                    myCalendar2.set(Calendar.DAY_OF_MONTH, day);

                    ((EditText)view).setText(dateFormat.format(myCalendar2.getTime()));
                }
            };
            new DatePickerDialog(getActivity(), date, myCalendar2.get(Calendar.YEAR), myCalendar2.get(Calendar.MONTH), myCalendar2.get(Calendar.DAY_OF_MONTH)).show();
        });

        // Xử lí nút quay về
        ((ImageView)root.findViewById(R.id.btnBack1)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
                // Nav cái này vẫn sài dc
            }
        });

        // Xử lý nút Lưu có vấn đề về đổ db
        ((Button)root.findViewById(R.id.btnSaveInfoStaff)).setOnClickListener(view ->{

            String cccd = edtId.getText().toString(),
            chucvu = positionSpinner.getSelectedItem().toString(),
            gioitinh = genderSpinner.getSelectedItem().toString(),
            hoten = edtName.getText().toString(),
            ngaysinh = edtDob.getText().toString(),
            ngayvl = edtBegin.getText().toString(),
            sdt = edtPhone.getText().toString(),
            hsl = edtHSL.getText().toString();


            if (getArguments() == null) // add mode
            {
                Map<String, Object> user = new HashMap<>();
                user.put("CCCD", cccd);
                user.put("CHUCVU", chucvu);
                user.put("GIOITINH", gioitinh);
                user.put("EMAIL", hsl);
                user.put("HOTEN", hoten);
                user.put("NGAYSINH", ngaysinh);
                user.put("NGVL", ngayvl);
                user.put("SDT", sdt);


                db.collection("/NHANVIEN/").add(user).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful())
                        {
                            ImageLoader.Upload("images/staff/" + task.getResult().getId() + ".jpg", avatar);
                        }
                    }
                });
            }
            else // edit mode
            {
                Map<String, Object> user = new HashMap<>();
                user.put("CCCD", cccd);
                user.put("CHUCVU", chucvu);
                user.put("GIOITINH", gioitinh);
                user.put("EMAIL", hsl);
                user.put("HOTEN", hoten);
                user.put("NGAYSINH", ngaysinh);
                user.put("NGVL", ngayvl);
                user.put("SDT", sdt);

                String id = getArguments().getString("MANV");

                db.collection("/NHANVIEN/").document(id).set(user);
                ImageLoader.Upload("images/staff/" + id + ".jpg", avatar);
            }

            Navigation.findNavController(view).navigate(R.id.action_fragment_staff_edit_to_fragment_staff);
        });

        return root;
    }


}
