package sg.edu.iss.mindmatters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.MimeTypeMap;
import android.webkit.ValueCallback;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import okhttp3.OkHttpClient;
import okhttp3.Response;

import okhttp3.Request;

public class QuizActivity extends BaseActivity {

    private final String mUrl = "http://10.0.2.2:8080/quiz/landing";
    private final String DONE_URL = "http://10.0.2.2:8080/resource/";
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        callCustomActionBar(QuizActivity.this,true);
        SharedPreferences pref = getSharedPreferences(
                "user_credentials", MODE_PRIVATE);


        mWebView = findViewById(R.id.web_view_quiz);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                String auth= "";
                if(pref.contains("token")) {
                    auth = pref.getString("token", null);
                }
                try {
                    OkHttpClient okHttpClient = new OkHttpClient();
                    Request requestNew = new Request.Builder().url(request.getUrl().toString()).addHeader("Authorization" , auth)
                            .build();
                    System.out.println(requestNew.headers());

                    Response response = okHttpClient.newCall(requestNew).execute();

                    return new WebResourceResponse(getMimeType(request.getUrl().toString()),
                            response.header("content-encoding", "utf-8"),
                            response.body().byteStream());
                }
                catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
                catch (Exception e){
                    return null;
                }
            }
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if(url.contains(DONE_URL)) {
                    finish();
                    if(pref.contains("token"))
                        saveNextDate(pref);
                    //clearCache();
                }
            }
        });

        //String data = "json="+"3";
        //mWebView.postUrl(mUrl, data.getBytes());

        mWebView.loadUrl(mUrl);

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

    private String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);

        if (extension != null) {

            switch (extension) {
                case "js":
                    return "text/javascript";
                case "woff":
                    return "application/font-woff";
                case "woff2":
                    return "application/font-woff2";
                case "ttf":
                    return "application/x-font-ttf";
                case "eot":
                    return "application/vnd.ms-fontobject";
                case "svg":
                    return "image/svg+xml";
            }

            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }

        return type;
    }
}