package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.model.userDTO;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends Activity {
    private EditText etPassword, etUsername;
    String passWord, userName;
    Button loginButton;
    TextView registerButton;
    TextView resetPwdButton;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null) {
            String uid = firebaseAuth.getCurrentUser().getUid();
            FirebaseFirestore.getInstance().collection("users").document(uid).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            MyGlobals.getInstance().setMyInfo(documentSnapshot.toObject(userDTO.class));
                        }
                    });
            finish();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }

        etUsername = (EditText) findViewById(R.id.txtLoginEmail);
        etPassword = (EditText) findViewById(R.id.txtLoginPassword);
        loginButton = findViewById(R.id.btnLogin);
        registerButton = findViewById(R.id.tv_register);
        resetPwdButton = findViewById(R.id.tv_find_id_pw);

        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                userName = etUsername.getText().toString();
                passWord = etPassword.getText().toString();
                if(userName.isEmpty())
                    Toast.makeText(LoginActivity.this, "이메일을 입력해주세요", Toast.LENGTH_SHORT).show();

                else if(passWord.isEmpty())
                    Toast.makeText(LoginActivity.this, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                else
                    loginUser(userName, passWord);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
        resetPwdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ResetPwdActivity.class);
                startActivity(intent);
            }
        });
    }

    public void loginUser(String userName, String passWord) {
        firebaseAuth.signInWithEmailAndPassword(userName, passWord)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String uid = firebaseAuth.getCurrentUser().getUid();
                            FirebaseFirestore.getInstance().collection("users").document(uid).get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            MyGlobals.getInstance().setMyInfo(documentSnapshot.toObject(userDTO.class));
                                        }
                                    });
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);

                        } else {
                            Toast.makeText(LoginActivity.this, "가입되지 않은 회원이거나 회원 정보가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    };
}
