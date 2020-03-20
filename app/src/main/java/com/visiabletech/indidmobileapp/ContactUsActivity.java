package com.visiabletech.indidmobileapp;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ContactUsActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Contact Us");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.back_img);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ContactUsActivity.this,MainPageActivity.class));
                finish();
            }
        });


        String html="<iframe src=\"https://www.google.com/maps/embed?pb=!1m14!1m8!1m3!1d230.17963050509118!2d88.34864116770387!3d22.621176597226658!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x0%3A0xb912a3629daac1b1!2sMotion%20Howrah%20IIT-JEE%20Main%7CNEET%20%7C%20Chemistry%20%7CBiology%20Coaching%20Institute%20kolkata!5e0!3m2!1sen!2sin!4v1582350991973!5m2!1sen!2sin\" width=\"100%\" height=\"100%\" frameborder=\"0\" style=\"border:0;\" allowfullscreen=\"\"></iframe>";

        webView = findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());
        webView.loadData(html,"text/html",null);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

    }


    @Override
    public void onBackPressed() {

        if (webView.canGoBack())
        {
            webView.goBack();
        }
        else
        {
            startActivity(new Intent(ContactUsActivity.this,MainPageActivity.class));
            finish();
        }


    }
}
