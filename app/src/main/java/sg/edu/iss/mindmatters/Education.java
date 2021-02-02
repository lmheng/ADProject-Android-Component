package sg.edu.iss.mindmatters;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class Education extends AppCompatActivity {

    private String mUrl;
    private WebView eduWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education);
        callCustomActionBar();
        Intent intent = getIntent();
        mUrl=intent.getStringExtra(Resources.EXTERNAL_EDU);
        eduWebView=findViewById(R.id.edu_web);
        eduWebView.getSettings().setJavaScriptEnabled(true);
        eduWebView.setWebViewClient(new WebViewClient());
        eduWebView.loadUrl(mUrl);


    }
    public void callCustomActionBar(){
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar_layout);
    }
}