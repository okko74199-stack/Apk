// MainActivity.java - محسن للشبكة المحلية
package com.barqnet.urlviewer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    
    private EditText urlInput;
    private Button saveButton, settingsButton, scanButton;
    private TextView networkStatus;
    private SharedPreferences preferences;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        preferences = getSharedPreferences("BarqNetPrefs", MODE_PRIVATE);
        
        urlInput = findViewById(R.id.urlInput);
        saveButton = findViewById(R.id.saveButton);
        settingsButton = findViewById(R.id.settingsButton);
        scanButton = findViewById(R.id.scanButton);
        networkStatus = findViewById(R.id.networkStatus);
        
        // عرض حالة الشبكة
        updateNetworkStatus();
        
        // تحميل الرابط المحفوظ
        String savedUrl = preferences.getString("saved_url", "");
        if (!savedUrl.isEmpty()) {
            openWebView(savedUrl);
        }
        
        setupClickListeners();
    }
    
    private void updateNetworkStatus() {
        if (NetworkUtils.isConnectedToLocalNetwork(this)) {
            String localIp = NetworkUtils.getLocalIpAddress(this);
            networkStatus.setText("متصل بالشبكة المحلية - IP: " + localIp);
            networkStatus.setTextColor(getColor(R.color.success));
        } else {
            networkStatus.setText("غير متصل بالشبكة المحلية");
            networkStatus.setTextColor(getColor(R.color.error));
        }
    }
    
    private void setupClickListeners() {
        saveButton.setOnClickListener(v -> saveUrl());
        settingsButton.setOnClickListener(v -> openSettings());
        scanButton.setOnClickListener(v -> scanLocalNetwork());
    }
    
    private void saveUrl() {
        String url = urlInput.getText().toString().trim();
        
        if (url.isEmpty()) {
            Toast.makeText(this, "يرجى إدخال عنوان URL", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // تنظيف الرابط للشبكة المحلية
        url = cleanLocalUrl(url);
        
        // حفظ الرابط
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("saved_url", url);
        editor.apply();
        
        // الانتقال المباشر
        openWebView(url);
    }
    
    private String cleanLocalUrl(String url) {
        // إزالة المسافات
        url = url.trim();
        
        // إضافة http:// إذا لم يكن موجوداً
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }
        
        // إضافة البورت الافتراضي إذا لم يكن موجوداً
        if (!url.contains(":") || url.endsWith(":")) {
            if (url.endsWith(":")) {
                url += "8000";
            } else {
                url += ":8000";
            }
        }
        
        return url;
    }
    
    private void openWebView(String url) {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("url", url);
        startActivity(intent);
        finish(); // منع العودة
    }
    
    private void openSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
    
    private void scanLocalNetwork() {
        // مسح للعناوين المحلية الشائعة
        String baseIp = NetworkUtils.getLocalIpAddress(this);
        String networkBase = baseIp.substring(0, baseIp.lastIndexOf("."));
        
        Toast.makeText(this, "مسح الشبكة: " + networkBase + ".x", Toast.LENGTH_SHORT).show();
        
        // عرض اقتراحات
        urlInput.setText(networkBase + ".100:8000");
    }
}
