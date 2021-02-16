package sg.edu.iss.mindmatters.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;

import sg.edu.iss.mindmatters.R;
import sg.edu.iss.mindmatters.model.Resource;
import sg.edu.iss.mindmatters.webview.MindfulnessList;

public class Recommended extends BaseActivity {
public static final String EXTERNAL_URL="externalUrl";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recommened_include);
        callCustomActionBar(Recommended.this, true);
        Intent intent=getIntent();
        String[] array=intent.getStringArrayExtra(Resources.EXTERNAL_URL_1);//(Resources.EXTERNAL_URL_1);
        String title_1=intent.getStringExtra("title");
        CarouselView carousel=findViewById(R.id.carousel);
        carousel.setPageCount(array.length);
        carousel.setImageListener(new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                Picasso.get().load("https://img.youtube.com/vi/" + array[position] + "/0.jpg").placeholder(R.drawable.ic_launcher_background).into(imageView);

            }
        });
        carousel.setImageClickListener(new ImageClickListener() {
            @Override
            public void onClick(int position) {
                String externalUrl ="http://10.0.2.2:8080/resource/view/"+array[position];
                launchExternalPage(externalUrl,title_1);
            }
        });
       /* Intent intent=getIntent();
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
        });*/

    }

    public void launchExternalPage(String externalurl,String title)
    {
        Intent intent=new Intent(Recommended.this, MindfulnessList.class);
        intent.putExtra("title",title);
        intent.putExtra(EXTERNAL_URL,externalurl);
        startActivity(intent);
    }


}