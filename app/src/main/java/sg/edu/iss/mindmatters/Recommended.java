package sg.edu.iss.mindmatters;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class Recommended extends ParentActivity {
public static final String EXTERNAL_URL="externalUrl";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recommened_include);
        callCustomActionBar();

        Intent intent=getIntent();
        String url_1=intent.getStringExtra(Resources.EXTERNAL_URL_1);
        String Url_code=url_1.substring(26,url_1.length()-6);
        String title_1=intent.getStringExtra("title");
        ImageView launchBtn = findViewById(R.id.recommended1);
        Picasso.get().load(url_1).placeholder(R.drawable.ic_launcher_background).into(launchBtn);
        launchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String externalUrl ="http://10.0.2.2:8080/resource/view/"+Url_code;
                launchExternalPage(externalUrl,title_1);
            }
        });
        String url_2=intent.getStringExtra(Resources.EXTERNAL_URL_2);
        ImageView launchBtn2 = findViewById(R.id.recommended2);
        Picasso.get().load(url_2).placeholder(R.drawable.ic_launcher_background).into(launchBtn2);
        String Url_code2=url_2.substring(26,url_2.length()-6);
        launchBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String externalUrl ="http://10.0.2.2:8080/resource/view/" +Url_code2;
                launchExternalPage(externalUrl,title_1);
            }
        });

    }

    public void launchExternalPage(String externalurl,String title)
    {
        Intent intent=new Intent(Recommended.this, MindfulnessList.class);
        intent.putExtra("title",title);
        intent.putExtra(EXTERNAL_URL,externalurl);
        startActivity(intent);
    }


}