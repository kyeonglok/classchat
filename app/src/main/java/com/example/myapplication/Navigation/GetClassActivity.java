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
import androidx.annotation.RequiresApi;
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
        findViewById(R.id.loadingPanel).setVisibility(View.GONE);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        mWebView = (WebView)findViewById(R.id.webView);
        mWebSettings = mWebView.getSettings();
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setDomStorageEnabled(true);
        mWebSettings.setSupportMultipleWindows(true);
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.setWebViewClient(new WebViewClient() {
            Handler delayHandler = new Handler();

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                if(url.equals("https://icampus.skku.edu/login")) {
                    delayHandler.postDelayed(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void run() {
                            mWebView.evaluateJavascript("javascript:(function() {" +
                                    "console.log(location.href);" +
                                    "if(location.href.includes('https://icampus.skku.edu/xn-sso/login.php'))" +
                                    "return 'failed';" +
                                    "})();", new ValueCallback<String>() {
                                @Override
                                public void onReceiveValue(String value) {
                                    if(value.equals("\"failed\"")) {
                                        Log.i("classchat", "Failed to login");
                                        Toast.makeText(GetClassActivity.this, "연동에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                        delayHandler.removeCallbacksAndMessages(null);
                                        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                                    }
                                }
                            });
                        }
                    }, 3000);
                }
                else if(url.equals("https://icampus.skku.edu/xn-sso/login.php?auto_login=true&sso_only=true&cvs_lgn=&return_url=https%3A%2F%2Ficampus.skku.edu%2Fxn-sso%2Fgw-cb.php%3Ffrom%3Dweb_redirect%26login_type%3Dstandalone%26return_url%3Dhttps%253A%252F%252Ficampus.skku.edu%252Flogin%252Fcallback")) {
                    delayHandler.postDelayed(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void run() {
                            mWebView.evaluateJavascript("javascript:(function() {" +
                                    "document.querySelector(\"#userid\").value='" + studentNum + "';" +
                                    "document.querySelector(\"#password\").value='" + studentPassword + "';" +
                                    "document.querySelector(\"#btnLoginBtn\").click();" +
                                    "})();", new ValueCallback<String>() {
                                @Override
                                public void onReceiveValue(String value) {

                                }
                            });
                        }
                    }, 500);
                }
                else if(url.equals("https://icampus.skku.edu/")) {
                    delayHandler.postDelayed(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void run() {
                            mWebView.evaluateJavascript("javascript:(function() {" +
                                    "location.replace('https://canvas.skku.edu');" +
                                    "})();", new ValueCallback<String>() {
                                @Override
                                public void onReceiveValue(String value) {

                                }
                            });
                        }
                    }, 500);
                }
                else if(url.equals("https://canvas.skku.edu/")) {
                    delayHandler.postDelayed(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void run() {
                            mWebView.evaluateJavascript("javascript:(function() {" +
                                    "var n = document.querySelector(\"#DashboardCard_Container > div\").childElementCount;" +
                                    "var cls = '';" +
                                    "for (var i = 1; i <= n; i++) {" +
                                    "if (document.querySelector(\"#DashboardCard_Container > div > div:nth-child(\" + i + \") > div > a > div > div.ic-DashboardCard__header-term.ellipsis\").title == '2020년 2학기')" +
                                    "cls = cls.concat(document.querySelector(\"#DashboardCard_Container > div > div:nth-child(\" + i + \") > div > a > div > div.ic-DashboardCard__header-subtitle.ellipsis\").title, '/');" +
                                    "}" +
                                    "return cls;" +
                                    "})();", new ValueCallback<String>() {
                                @Override
                                public void onReceiveValue(String value) {
                                    Log.i("classchat", value);
                                    HashMap<String, String> classes = new HashMap<>();
                                    String[] result = value.replace("\"", "").split("/");
                                    for (int i = 0; i < result.length; i++) {
                                        int idx = result[i].indexOf('_');
                                        classes.put(result[i].substring(idx + 1, result[i].lastIndexOf('(')), result[i].substring(0, idx));
                                    }
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
                                    findViewById(R.id.loadingPanel).setVisibility(View.GONE);

                                }
                            });
                        }
                    }, 500);
                }
            }
        });

        mWebView.setVisibility(View.GONE);
        mWebView.clearCache(true);
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
                else {
                    findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
                    mWebView.loadUrl("https://icampus.skku.edu/login");
                }
            }
        });
    }
}
