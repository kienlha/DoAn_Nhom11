package com.example.doannhom11;

import static androidx.fragment.app.FragmentManager.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity_SignUp extends AppCompatActivity {

    ImageView btnBack;
    EditText Email, Password, PasswordAgain, ShopName;
    Button btnSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_sign_up);
        btnBack = findViewById(R.id.btnBacked);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity_SignUp.this, MainActivity.class);
                startActivity(intent);
            }
        });
        unitUI();
        initUIListen();
    }
    private void unitUI()
    {
        Email= findViewById(R.id.edtUsername);
        Password = findViewById(R.id.edtNewPw);
        btnSignUp = findViewById(R.id.btnInputSignUp);
        PasswordAgain = findViewById(R.id.edtPwAgain);
        ShopName = findViewById(R.id.edtShopName);
    }
    private void initUIListen()
    {
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSignUp();
            }
        });
    }

    private void onClickSignUp() {
        String email= Email.getText().toString().trim();
        String password = Password.getText().toString().trim();
        String repassword = PasswordAgain.getText().toString().trim();
        String shopname = ShopName.getText().toString().trim();
        if(email.isEmpty()||password.isEmpty()||repassword.isEmpty()||shopname.isEmpty())
        {}
        else {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Intent intent = new Intent(MainActivity_SignUp.this, MainActivity2.class);
                                startActivity(intent);
                                FirebaseUser user = mAuth.getCurrentUser();
                                finishAffinity();
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(MainActivity_SignUp.this, "Đăng kí thất bại",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}