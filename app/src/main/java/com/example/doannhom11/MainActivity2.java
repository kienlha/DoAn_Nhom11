package com.example.doannhom11;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity2 extends AppCompatActivity {

    private FirebaseAuth mAuth;

    DocumentReference db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance().document("CUAHANG/" + mAuth.getUid());
        if(mAuth.getCurrentUser() == null){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        // Khi click vào nút menu thì mở ra cái Nav
        findViewById(R.id.imageMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        // Dang xuat
        findViewById(R.id.imageLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity2.this);
                builder.setIcon(R.drawable.ic_baseline_exit_to_app_blue);
                builder.setTitle("Đăng xuất");
                builder.setMessage("Bạn muốn đăng xuất?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mAuth.signOut();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();
            }
        });
        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.getHeaderView(0).findViewById(R.id.tvNameCf);
        ((TextView)navigationView.getHeaderView(0).findViewById(R.id.tvNameCf)).setText("TEN_CUAHANG");

        db.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    ((TextView)navigationView.getHeaderView(0).findViewById(R.id.tvNameCf)).setText(task.getResult().getString("TEN_CUAHANG"));

                }
            }
        });
        navigationView.setItemIconTintList(null);
        NavController controller = Navigation.findNavController(this, R.id.fragmentContainerView3);
        NavigationUI.setupWithNavController(navigationView, controller);

    }
}