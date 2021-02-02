package sg.edu.iss.mindmatters;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class MindfulnessList extends ParentActivity {
private WebView mindfullist;
private String mUrl;
private TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mindfulness_list);
        callCustomActionBar();
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