package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.webkit.WebSettingsCompat;
import androidx.webkit.WebViewFeature;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.WebBackForwardList;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    WebView webView;
    ProgressBar progressBar;
    WebSettings webSettings;
    Button button;
    RelativeLayout relativeLayout,relativeLayout1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.web_view);
        progressBar = findViewById(R.id.progressBar);
        relativeLayout = findViewById(R.id.nointernet);
        relativeLayout1 = findViewById(R.id.splash);
        button = findViewById(R.id.reload);
        relativeLayout.setVisibility(View.GONE);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new myWebViewClient());
        webView.loadUrl("https://konsultasi.arisumardi.my.id/");
        if(WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
            WebSettingsCompat.setForceDark(webView.getSettings(), WebSettingsCompat.FORCE_DARK_OFF);
        }
    }

    public class myWebViewClient extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            progressBar.setVisibility(View.VISIBLE);
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//            Toast.makeText(getApplicationContext(), "Koneksi Internet Tidak ada", Toast.LENGTH_SHORT).show();
            relativeLayout.setVisibility(View.VISIBLE);
            isconnected();

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.this.webView.loadUrl(failingUrl);
                    relativeLayout.setVisibility(View.GONE);
                }
            });
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            isconnected();
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
            splash();
        }
    }



    @Override
    public void onBackPressed() {
        if (webView.getUrl().equals("https://konsultasi.arisumardi.my.id/login") || webView.getUrl().equals("https://konsultasi.arisumardi.my.id/") || webView.getUrl().equals("https://konsultasi.arisumardi.my.id/user/home")) {
            alert();
        } else{
            webView.goBack();
        }
    }

    public void isconnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED){
            progressBar.setVisibility(View.VISIBLE);
            webView.setVisibility(View.VISIBLE);
        }else {
            webView.setVisibility(View.GONE);
        }
    }

    public void alert(){
        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Confirm")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setNegativeButton("No", null).show();
    }

    public void splash(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                relativeLayout1.setVisibility(View.GONE);
            }
        }, 1000L);
    }
}