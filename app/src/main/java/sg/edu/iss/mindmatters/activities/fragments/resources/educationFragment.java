package sg.edu.iss.mindmatters.activities.fragments.resources;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import sg.edu.iss.mindmatters.R;

public class educationFragment extends Fragment {

    private String mUrl;
    private WebView eduWebView;

    String[] eduLink;
    View mView;

    public educationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_education, container, false);
        this.mView = view;

        eduLink = getArguments().getStringArray("resources");

        mUrl=eduLink[0];
        eduWebView=mView.findViewById(R.id.edu_web);
        eduWebView.setBackgroundColor(0);
        eduWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        eduWebView.getSettings().setJavaScriptEnabled(true);
        eduWebView.setWebViewClient(new WebViewClient());
        eduWebView.loadUrl(mUrl);

        return view;
    }
}