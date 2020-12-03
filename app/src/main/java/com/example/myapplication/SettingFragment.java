package com.example.myapplication;

import android.content.Context;
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
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

public class SettingFragment extends Fragment {
    private View view;
    private Context context;
    private WebView mWebView;
    private WebSettings mWebSettings;
    private EditText etPassword, etNumber;
    String studentNum, studentPassword;
    Button syncButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_setting,container,false);
        context = container.getContext();

        mWebView = (WebView)view.findViewById(R.id.webView);
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
                                    if(s.trim().equals("\"success\"")) {
                                        Toast.makeText(context, "학수 정보를 성공적으로 불러왔습니다", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Toast.makeText(context, "학수 정보를 불러오지 못했습니다", Toast.LENGTH_SHORT).show();
                                    }
                                }

                            });
                        }
                    }
                }, 1000);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    public void run() {
                        mWebView.evaluateJavascript("javascript:(function() {" +
                                "console.log(parent.parent.parent.document.getElementsByName('Main')[0].contentWindow.document.getElementsByName('contentFrame')[0].contentWindow.document.getElementsByName('mainFrame')[0].contentWindow.document.getElementsByTagName('html')[0].childNodes[2].getElementsByTagName('table'));" +
                                "var len = parent.parent.parent.document.getElementsByName('Main')[0].contentWindow.document.getElementsByName('contentFrame')[0].contentWindow.document.getElementsByName('mainFrame')[0].contentWindow.document.getElementsByTagName('html')[0].childNodes[2].getElementsByTagName('table')[7].getElementsByTagName('tbody')[0].getElementsByTagName('tr').length;" +
                                "console.log(len);" +
                                "var i = 1;" +
                                "var str = '';" +
                                "for (i = 1; i < len; i++) {" +
                                "str.concat(parent.parent.parent.document.getElementsByName('Main')[0].contentWindow.document.getElementsByName('contentFrame')[0].contentWindow.document.getElementsByName('mainFrame')[0].contentWindow.document.getElementsByTagName('html')[0].childNodes[2].getElementsByTagName('table')[7].getElementsByTagName('tbody')[0].getElementsByTagName('tr')[i].getElementsByTagName('td')[3].title);" +
                                "console.log(parent.parent.parent.document.getElementsByName('Main')[0].contentWindow.document.getElementsByName('contentFrame')[0].contentWindow.document.getElementsByName('mainFrame')[0].contentWindow.document.getElementsByTagName('html')[0].childNodes[2].getElementsByTagName('table')[7].getElementsByTagName('tbody')[0].getElementsByTagName('tr')[i].getElementsByTagName('td')[3].title);" +
                                "}" +
                                "return str;" +

                                "})();", new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String s) {
                                Log.i("hello", s);
                            }

                        });
                    }
                }, 4000);
            }
        });
        // mWebView.setVisibility(View.GONE);
        etNumber = (EditText) view.findViewById(R.id.txtStudentNum);
        etPassword = (EditText) view.findViewById(R.id.txtStudentPassword);
        syncButton = view.findViewById(R.id.btnSync);

        syncButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                studentNum = etNumber.getText().toString();
                studentPassword = etPassword.getText().toString();

                if(studentNum.isEmpty())
                    Toast.makeText(context, "학번을 입력해주세요", Toast.LENGTH_SHORT).show();
                else if(studentPassword.isEmpty())
                    Toast.makeText(context, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                else
                    mWebView.loadUrl("https://sugang.skku.edu/skku");
            }
        });

        return view;
    }

}
