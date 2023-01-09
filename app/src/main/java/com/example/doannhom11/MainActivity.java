package com.example.doannhom11;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    TextView btnSignUp;
    Button btnSignIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignIn = findViewById(R.id.btnInput);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MainActivity_SignUp.class);
                startActivity(intent);
            }
        });
       unitListen();

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                show.setVisibility(view.INVISIBLE);
                hide.setVisibility(view.VISIBLE);
            }
        });
        hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                hide.setVisibility(view.INVISIBLE);
                show.setVisibility(view.VISIBLE);
            }
        });
    }
     private void unitListen()
    {
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSignIn();
            }
        });
    }
    private void onClickSignIn() {
        String email= Email.getText().toString().trim();
        String password = Password.getText().toString().trim();
        if(email.isEmpty()||password.isEmpty())
        {
            Toast.makeText(MainActivity.this, "Thông tin thiếu",
                    Toast.LENGTH_SHORT).show();
        }
       else
        {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                                startActivity(intent);
                                FirebaseUser user = mAuth.getCurrentUser();
                                finishAffinity();
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(MainActivity.this, "Vui lòng kiểm tra lại thông tin",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}
