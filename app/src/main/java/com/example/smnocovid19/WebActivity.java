package com.example.smnocovid19;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Html;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

public class WebActivity extends AppCompatActivity {
    ProgressDialog progress;
    WebSettings webSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // 화면을 portrait(세로) 화면으로 고정하고 싶은 경우


        setContentView(R.layout.activity_web);

        //액션바 안보이게 지정
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Intent intent = getIntent();
        String strLink = intent.getStringExtra("site_url");
        String printMsg = intent.getStringExtra("print_msg");

        WebView web = (WebView)findViewById(R.id.web);

        progress = new ProgressDialog(this);
        progress.setMessage("페이지를 불러오는 중입니다..");
        progress.show();
        webSettings=web.getSettings();
        webSettings.setJavaScriptEnabled(true);
        web.setWebViewClient(new MyWebView());
        web.loadUrl(strLink);

        Toast.makeText(WebActivity.this, printMsg, Toast.LENGTH_SHORT).show();
    }

    public class MyWebView extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url){
            System.out.println(url +"-----------------------테스트--------------------");
            return super.shouldOverrideUrlLoading(view,url);
        }
        public void onPageFinished(WebView view, String url){
            progress.dismiss();
            super.onPageFinished(view,url);
        }
    }
}