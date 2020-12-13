package com.example.myapplication.Navigation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.MyGlobals;
import com.example.myapplication.R;
import com.example.myapplication.RegisterActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class ChangeNicknameActivity extends AppCompatActivity {
    Button checkNicknameButton, changeButton, backButton;
    EditText etNickname;
    String checkedNickname = null, nickname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_nickname);

        etNickname = findViewById(R.id.et_nickname);
        checkNicknameButton = findViewById(R.id.btn_check_nickname);
        changeButton = findViewById(R.id.btn_change);
        backButton = findViewById(R.id.btn_back);

        checkNicknameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nickname = etNickname.getText().toString();
                if(nickname.isEmpty()) {
                    Toast.makeText(ChangeNicknameActivity.this, "닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else {
                    FirebaseFirestore.getInstance().collection("users")
                            .whereEqualTo("nickname", nickname)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        if (task.getResult().size() != 0)
                                            Toast.makeText(ChangeNicknameActivity.this, "이미 존재하는 닉네임입니다.", Toast.LENGTH_SHORT).show();
                                        else {
                                            checkedNickname = nickname;
                                            Toast.makeText(ChangeNicknameActivity.this, "사용할 수 있는 닉네임입니다.", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(ChangeNicknameActivity.this, "다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nickname = etNickname.getText().toString();
                if(nickname.isEmpty()) {
                    Toast.makeText(ChangeNicknameActivity.this, "닉네임을 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(checkedNickname == null || !checkedNickname.equals(nickname)) {
                    Toast.makeText(ChangeNicknameActivity.this, "닉네임 중복 확인을 해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                FirebaseFirestore.getInstance().collection("users").document(uid)
                        .update("nickname", nickname)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                MyGlobals.getInstance().setMyNickname(nickname);
                                finish();
                                Toast.makeText(ChangeNicknameActivity.this, "변경에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ChangeNicknameActivity.this, "변경에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });

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