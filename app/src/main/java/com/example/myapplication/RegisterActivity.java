package com.example.myapplication;
import com.example.myapplication.Model.userDTO;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.Navigation.GetClassActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {

    private EditText etPassword, etUsername, etNickname, etPasswordCheck;
    String userName, nickName, passWord, passWordCheck;
    Button registerButton;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        etUsername = (EditText) findViewById(R.id.txtRegisterEmail);
        etNickname = (EditText) findViewById(R.id.txtRegisterNickname);
        etPassword = (EditText) findViewById(R.id.txtRegisterPassword);
        etPasswordCheck = (EditText) findViewById(R.id.txtRegisterPasswordCheck);
        registerButton = findViewById(R.id.btnRegister2);

        registerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                userName = etUsername.getText().toString();
                nickName = etNickname.getText().toString();
                passWord = etPassword.getText().toString();
                passWordCheck = etPasswordCheck.getText().toString();

                if(userName.isEmpty())
                    Toast.makeText(RegisterActivity.this, "이메일을 입력해주세요", Toast.LENGTH_SHORT).show();
                else if(nickName.isEmpty())
                    Toast.makeText(RegisterActivity.this, "닉네임을 입력해주세요", Toast.LENGTH_SHORT).show();
                else if(passWord.isEmpty())
                    Toast.makeText(RegisterActivity.this, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                else if(!passWord.equals(passWordCheck))
                    Toast.makeText(RegisterActivity.this, "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
                else
                    registerUser(userName, passWord);
            }
        });

    }

    public void registerUser(String userName, String passWord) {
        Log.i("name ", userName);
        Log.i("password ", passWord);
        firebaseAuth.createUserWithEmailAndPassword(userName, passWord)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String uid = firebaseAuth.getCurrentUser().getUid();
                            userDTO currentUser = new userDTO();
                            String nickname = etNickname.getText().toString();
                            String email = firebaseAuth.getCurrentUser().getEmail();
                            currentUser.setNickname(nickname);
                            currentUser.setEmail(email);
                            firebaseFirestore.collection("users").document(uid).set(currentUser);
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(RegisterActivity.this, "회원가입에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(RegisterActivity.this, "이미 등록된 회원이거나 올바른 형식이 아닙니다", Toast.LENGTH_SHORT).show();
                    }
                });

    };
}