package sg.edu.iss.mindmatters.webview;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import sg.edu.iss.mindmatters.R;
import sg.edu.iss.mindmatters.activities.Resources;
import sg.edu.iss.mindmatters.activities.BaseActivity;

public class Education extends BaseActivity {

    private String mUrl;
    private WebView eduWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education);
        callCustomActionBar(Education.this, true);

        Intent intent = getIntent();
        mUrl=intent.getStringExtra(Resources.EXTERNAL_EDU);
        eduWebView=findViewById(R.id.edu_web);
        eduWebView.getSettings().setJavaScriptEnabled(true);
        eduWebView.setWebViewClient(new WebViewClient());
        eduWebView.loadUrl(mUrl);
    }

}