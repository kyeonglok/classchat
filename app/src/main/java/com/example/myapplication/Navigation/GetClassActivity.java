package com.example.myapplication.Navigation;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Model.userDTO;
import com.example.myapplication.R;
import com.example.myapplication.RegisterActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class GetClassActivity extends AppCompatActivity {
    private WebView mWebView;
    private WebSettings mWebSettings;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    private EditText etPassword, etNumber;
    String studentNum, studentPassword;
    Button syncButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage_get_classes);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        mWebView = (WebView)findViewById(R.id.webView);
        mWebSettings = mWebView.getSettings();
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setDomStorageEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                /*
                Handler delayHandler = new Handler();
                delayHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // TODO
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            mWebView.evaluateJavascript("javascript:(function() {" +
                                    "document.getElementsByName('Main')[0].contentWindow.document.getElementById('id').value='" + studentNum + "';" + // 학번 입력
                                    "document.getElementsByName('Main')[0].contentWindow.document.getElementById('pwd').value='" + studentPassword + "';" + // PASSWORD 입력
                                    "document.getElementsByName('Main')[0].contentWindow.document.getElementById('btn_login').click();" + // LOGIN 버튼 클릭
                                    //"if (document.getElementsByName('Main')[0].contentWindow.document.getElementsByName('contentFrame')[0] === undefined)" +
                                    //"return 'failed';" +
                                    "setTimeout(function() {" +
                                    "console.log('helloworld');" +
                                    "document.getElementsByName('Main')[0].contentWindow.document.getElementsByName('contentFrame')[0].contentWindow.document.getElementsByName('topFrame')[0].contentWindow.document.getElementsByTagName('li')[2].childNodes[0].click();" +
                                    "}, 1000);" +
                                    "return 'success';" +

                                    "})();", new ValueCallback<String>() {
                                @Override
                                public void onReceiveValue(String s) {
                                    if (s.trim().equals("\"success\"")) {
                                        Toast.makeText(GetClassActivity.this, "학수 정보를 성공적으로 불러왔습니다", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(GetClassActivity.this, "학수 정보를 불러오지 못했습니다", Toast.LENGTH_SHORT).show();
                                    }
                                }

                            });
                        }
                    }
                }, 500);
                */

                HashMap<String, String> classes = new HashMap<>();
                classes.put("ICE3037-43", "종합설계프로젝트");
                String uid = firebaseAuth.getCurrentUser().getUid();
                firebaseFirestore.collection("users").document(uid)
                        .update("classes", classes)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                finish();
                                Toast.makeText(GetClassActivity.this, "연동에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(GetClassActivity.this, "연동에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        mWebView.setVisibility(View.GONE);
        etNumber = (EditText) findViewById(R.id.et_email);
        etPassword = (EditText) findViewById(R.id.et_code);
        syncButton = findViewById(R.id.btn_ok);

        syncButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                studentNum = etNumber.getText().toString();
                studentPassword = etPassword.getText().toString();

                if(studentNum.isEmpty())
                    Toast.makeText(GetClassActivity.this, "학번을 입력해주세요", Toast.LENGTH_SHORT).show();
                else if(studentPassword.isEmpty())
                    Toast.makeText(GetClassActivity.this, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                else
                    mWebView.loadUrl("https://sugang.skku.edu/skku");
            }
        });
    }
}
