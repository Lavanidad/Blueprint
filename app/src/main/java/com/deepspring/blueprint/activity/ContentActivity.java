package com.deepspring.blueprint.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.deepspring.blueprint.R;


public class ContentActivity extends AppCompatActivity {

    private WebView webView;
    private String uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        webView = findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        uri = bundle.getString("uri");
        webView.loadUrl(uri);
    }

}
