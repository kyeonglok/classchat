package com.example.myapplication.navigation;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.LoginActivity;
import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;

public class SettingFragment extends Fragment {
    private Button logoutButton;
    private View view;
    private Context context;
    private WebView mWebView;
    private WebSettings mWebSettings;
    private EditText etPassword, etNumber;
    private FirebaseAuth auth;
    String studentNum, studentPassword;
    Button syncButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_setting, container, false);
        context = container.getContext();
        auth = FirebaseAuth.getInstance();
        logoutButton = (Button) view.findViewById(R.id.btn_logout);
        mWebView = (WebView) view.findViewById(R.id.webView);
        mWebSettings = mWebView.getSettings();
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setDomStorageEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
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

                                    "if (document.getElementsByName('Main')[0].contentWindow.document.getElementsByName('contentFrame')[0] === undefined)" +
                                    "return 'failed';" +
                                    "console.log(document.getElementsByName('Main')[0].contentWindow.document.getElementById('message'));" + // LOGIN 버튼 클릭
                                    "document.getElementsByName('Main')[0].contentWindow.document.getElementsByName('contentFrame')[0].contentWindow.document.getElementsByName('topFrame')[0].contentWindow.document.getElementsByTagName('li')[2].childNodes[0].click();" +
                                    "var len = parent.parent.parent.document.getElementsByName('Main')[0].contentWindow.document.getElementsByName('contentFrame')[0].contentWindow.document.getElementsByName('mainFrame')[0].contentWindow.document.getElementsByTagName('html')[0].childNodes[2].getElementsByTagName('table')[7].getElementsByTagName('tbody')[0].getElementsByTagName('tr').length;" +
                                    "var i = 1;" +
                                    "for (i = 1; i < len; i++) {" +
                                    "console.log(parent.parent.parent.document.getElementsByName('Main')[0].contentWindow.document.getElementsByName('contentFrame')[0].contentWindow.document.getElementsByName('mainFrame')[0].contentWindow.document.getElementsByTagName('html')[0].childNodes[2].getElementsByTagName('table')[7].getElementsByTagName('tbody')[0].getElementsByTagName('tr')[i].getElementsByTagName('td')[3].title);" +
                                    "}" +
                                    "return 'success';" +

                                    "})();", new ValueCallback<String>() {
                                @Override
                                public void onReceiveValue(String s) {
                                    Log.i("hello", s);
                                    if (s.trim().equals("\"success\"")) {
                                        Toast.makeText(context, "학수 정보를 성공적으로 불러왔습니다", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(context, "학수 정보를 불러오지 못했습니다", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                }, 500);
            }
        });
        mWebView.setVisibility(View.GONE);
        etNumber = (EditText) view.findViewById(R.id.txtStudentNum);
        etPassword = (EditText) view.findViewById(R.id.txtStudentPassword);
        syncButton = view.findViewById(R.id.btnSync);

        syncButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                studentNum = etNumber.getText().toString();
                studentPassword = etPassword.getText().toString();

                if (studentNum.isEmpty())
                    Toast.makeText(context, "학번을 입력해주세요", Toast.LENGTH_SHORT).show();
                else if (studentPassword.isEmpty())
                    Toast.makeText(context, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                else
                    mWebView.loadUrl("https://sugang.skku.edu/skku");
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
                    auth.signOut();
            }
        });
        return view;
    }

}
