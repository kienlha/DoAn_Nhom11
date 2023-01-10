package com.example.doannhom11;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    TextView btnSignUp;
    Button btnSignIn;
    ImageView hide, show;
    EditText edtMail, edtPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignIn = findViewById(R.id.btnInput);
        hide = findViewById(R.id.hidepw);
        show = findViewById(R.id.showpw);
        edtMail = findViewById(R.id.inputUsername);
        edtPass = findViewById(R.id.inputPassword);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MainActivity_SignUp.class);
                startActivity(intent);
            }
        });
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                show.setVisibility(view.INVISIBLE);
                hide.setVisibility(view.VISIBLE);
            }
        });
        hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                hide.setVisibility(view.INVISIBLE);
                show.setVisibility(view.VISIBLE);
            }
        });
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSignUp();
            }
        });
    }

    private void onClickSignUp() {
        String email = edtMail.getText().toString().trim();
        String password = edtPass.getText().toString().trim();
        if(email.isEmpty()||email==null||password.isEmpty()||password==null)
        {
            Toast.makeText(MainActivity.this, "Vui lòng điền đầy đủ thông tin đăng nhập",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent intent= new Intent(MainActivity.this, MainActivity2.class);
                                startActivity(intent);
                            } else {
                                //Test customToast
                                CustomToast.e(MainActivity.this, "Vui lòng kiểm tra lại thông tin.",
                                        Toast.LENGTH_SHORT);
                            }
                        }
                    });
        }
    }

}
