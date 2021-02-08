package sg.edu.iss.mindmatters.webview;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import sg.edu.iss.mindmatters.activities.Mindfulness;
import sg.edu.iss.mindmatters.R;
import sg.edu.iss.mindmatters.activities.BaseActivity;

public class MindfulnessList extends BaseActivity {
private WebView mindfullist;
private String mUrl;
private TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mindfulness_list);
        callCustomActionBar(MindfulnessList.this, true);
        Intent intent = getIntent();
        String name=intent.getStringExtra("title");
        title=(TextView)findViewById(R.id.mindfultype);
        title.setText(name);
        mUrl=intent.getStringExtra(Mindfulness.EXTERNAL_URL);
        mindfullist=findViewById(R.id.mindfullist);
        mindfullist.getSettings().setJavaScriptEnabled(true);
        mindfullist.setWebViewClient(new WebViewClient());
        mindfullist.loadUrl(mUrl);
    }

}