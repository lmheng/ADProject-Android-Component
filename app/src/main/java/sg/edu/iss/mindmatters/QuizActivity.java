package sg.edu.iss.mindmatters;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class QuizActivity extends BaseActivity {

    private final String mUrl = "http://10.0.2.2:8080/quiz/landing";
    private final String DONE_URL = "http://10.0.2.2:8080/resource/list/all";
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        MyApplication.setCurrentActivity("QuizActivity");
        this.callCustomActionBar();
        SharedPreferences pref = getSharedPreferences(
                "user_credentials", MODE_PRIVATE);


        mWebView = findViewById(R.id.web_view);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {
            //to prevent users from accessing any other website
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if(url.contains(DONE_URL)) {
                    finish();
                    saveNextDate(pref);
                    //clearCache();
                }
            }
        });

        //String data = "json="+"3";
        //mWebView.postUrl(mUrl, data.getBytes());

        String data = "Authorization=" + pref.getString("token", null);
        mWebView.postUrl(mUrl,data.getBytes());

    }

    public void saveNextDate(SharedPreferences pref){
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.add(Calendar.MONTH, 3);
        DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        editor.putString(pref.getString("username","user"), sdf.format(today.getTime()));
        editor.commit();
    }

    public void clearCache(){
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookies(new ValueCallback<Boolean>() {
            // a callback which is executed when the cookies have been removed
            @Override
            public void onReceiveValue(Boolean aBoolean) {
                Log.d("Cookie", "Cookie removed: " + aBoolean);
            }});
        mWebView.clearCache(true);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        //clearCache();
        finish();
    }


    public void callCustomActionBar() {
        callCustomActionBar(QuizActivity.this,false);
        findViewById(R.id.actionBarTitle).setVisibility(View.GONE);
    }
}