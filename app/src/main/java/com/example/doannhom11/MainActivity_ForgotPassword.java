package com.example.doannhom11;

import static androidx.fragment.app.FragmentManager.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity_ForgotPassword extends AppCompatActivity {
    ImageView btnBack;
    TextView btnSignUp;
    Button btnSend;
    EditText Email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_forgot_password);
        btnBack = findViewById(R.id.btnBacked);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnSend = findViewById(R.id.btnSend);
        Email = findViewById(R.id.inputUsername);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity_ForgotPassword.this, MainActivity.class);
                startActivity(intent);
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity_ForgotPassword.this, MainActivity_SignUp.class);
                startActivity(intent);
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickForgotPassword();
            }
        });

    }
    private void onClickForgotPassword() {

        String email= Email.getText().toString().trim();
        if(email.isEmpty()||email==null)
        {
            Toast.makeText(MainActivity_ForgotPassword.this,"Vui lòng nhập email", Toast.LENGTH_SHORT).show();
        }
        else {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(MainActivity_ForgotPassword.this, "Đã gửi đến Email", Toast.LENGTH_SHORT).show();
                            } else
                            {
                                Toast.makeText(MainActivity_ForgotPassword.this,"Vui lòng kiểm tra lại email", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }

}
