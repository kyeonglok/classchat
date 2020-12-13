package com.example.myapplication.Navigation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.MyGlobals;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePwdActivity extends AppCompatActivity {

    Button changeButton, backButton;
    EditText etCurrentPwd, etChangePwd, etPwdCheck;
    String curpwd, changepwd, pwdcheck;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);

        etCurrentPwd = findViewById(R.id.et_curpwd);
        etChangePwd = findViewById(R.id.et_changepwd);
        etPwdCheck = findViewById(R.id.et_pwdcheck);
        changeButton = findViewById(R.id.btn_change);
        backButton = findViewById(R.id.btn_back);

        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curpwd = etCurrentPwd.getText().toString();
                changepwd = etChangePwd.getText().toString();
                pwdcheck = etPwdCheck.getText().toString();

                if(curpwd.isEmpty()) {
                    Toast.makeText(ChangePwdActivity.this, "현재 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else if(changepwd.isEmpty()) {
                    Toast.makeText(ChangePwdActivity.this, "새 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else if(pwdcheck.isEmpty()) {
                    Toast.makeText(ChangePwdActivity.this, "새 비밀번호를 한 번 더 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else if(!changepwd.equals(pwdcheck)) {
                    Toast.makeText(ChangePwdActivity.this, "새 비밀번호가 서로 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
                else if(curpwd.equals(changepwd)) {
                    Toast.makeText(ChangePwdActivity.this, "새 비밀번호를 현재 비밀번호와 다르게 설정해주세요.", Toast.LENGTH_SHORT).show();
                }
                else {
                    AuthCredential credential = EmailAuthProvider
                            .getCredential(MyGlobals.getInstance().getMyEmail(), curpwd);

                    user = FirebaseAuth.getInstance().getCurrentUser();
                    user.reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        user.updatePassword(changepwd).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    finish();
                                                    Toast.makeText(ChangePwdActivity.this, "비밀번호를 변경하였습니다.", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(ChangePwdActivity.this, "비밀번호 변경에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    } else {
                                        Toast.makeText(ChangePwdActivity.this, "비밀번호 변경에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}