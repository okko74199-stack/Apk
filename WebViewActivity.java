// WebViewActivity.java - مع تحسينات للشبكة المحلية
package com.barqnet.urlviewer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class WebViewActivity extends AppCompatActivity {
    
    private WebView webView;
    
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        
        webView = findViewById(R.id.webView);
        
        // إعدادات متقدمة للشبكة المحلية
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        
        // WebViewClient مخصص للشبكة المحلية
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // السماح فقط بالروابط المحلية
                if (isLocalUrl(url)) {
                    view.loadUrl(url);
                } else {
                    Toast.makeText(WebViewActivity.this, "مسموح فقط بالروابط المحلية", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
            
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                // معالجة أخطاء الشبكة المحلية
                if (errorCode == ERROR_CONNECT || errorCode == ERROR_HOST_LOOKUP) {
                    Toast.makeText(WebViewActivity.this, "لا يمكن الوصول إلى الخادم المحلي", Toast.LENGTH_LONG).show();
                    // العودة للشاشة الرئيسية بعد خطأ
                    Intent intent = new Intent(WebViewActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        
        // تحميل الرابط
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("url")) {
            String url = intent.getStringExtra("url");
            webView.loadUrl(url);
        }
    }
    
    private boolean isLocalUrl(String url) {
        // التحقق إذا كان الرابط محلي
        return url.contains("192.168.") || 
               url.contains("10.0.") || 
               url.contains("172.16.") ||
               url.contains("172.17.") ||
               url.contains("172.18.") ||
               url.contains("172.19.") ||
               url.contains("172.20.") ||
               url.contains("172.21.") ||
               url.contains("172.22.") ||
               url.contains("172.23.") ||
               url.contains("172.24.") ||
               url.contains("172.25.") ||
               url.contains("172.26.") ||
               url.contains("172.27.") ||
               url.contains("172.28.") ||
               url.contains("172.29.") ||
               url.contains("172.30.") ||
               url.contains("172.31.") ||
               url.contains("localhost") ||
               url.contains("127.0.0.1");
    }
    
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
