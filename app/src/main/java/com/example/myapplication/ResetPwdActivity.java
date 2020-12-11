package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPwdActivity extends AppCompatActivity {
    private EditText etEmail;
    Button sendMailButton, backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pwd);
        etEmail = findViewById(R.id.et_email);
        sendMailButton = findViewById(R.id.btn_send_email);
        backButton = findViewById(R.id.btn_back);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        sendMailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                findPassword(email);
            }
        });
    }
    public void findPassword(String email) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            finish();
                            Toast.makeText(ResetPwdActivity.this, "비밀번호 재설정 메일을 전송하였습니다.", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(ResetPwdActivity.this, "가입되지 않은 이메일입니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    };
}