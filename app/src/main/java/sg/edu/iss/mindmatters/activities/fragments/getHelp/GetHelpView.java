package sg.edu.iss.mindmatters.activities.fragments.getHelp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import sg.edu.iss.mindmatters.R;

public class GetHelpView extends AppCompatActivity {
    private String mUrl;
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        mUrl = intent.getStringExtra(GetHelpAdapter.EXTERNAL_URL);


        // Initialize webview and launch the url
        mWebView = findViewById(R.id.web_view);

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.loadUrl(mUrl);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        else{
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

}