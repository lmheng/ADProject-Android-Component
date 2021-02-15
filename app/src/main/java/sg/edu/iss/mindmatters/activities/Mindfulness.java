package sg.edu.iss.mindmatters.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;

import java.lang.reflect.Array;
import java.util.Random;

import sg.edu.iss.mindmatters.R;
import sg.edu.iss.mindmatters.Results_temp;
import sg.edu.iss.mindmatters.webview.MindfulnessList;

public class Mindfulness extends BaseActivity implements View.OnClickListener {
ImageView afraid;
ImageView anxious;
ImageView sleep;
ImageView panic;
ImageView ocd;
ImageView depressed;
ImageView launchBtn;//
ImageView launchBtn2;//
String Url_code;
String Url_code2;
String title;

public static final String EXTERNAL_URL="externalUrl";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mindfulness);
        callCustomActionBar(Mindfulness.this, true);
        afraid=(ImageView)findViewById(R.id.afraidbtn);
        afraid.setOnClickListener(this);
        anxious=(ImageView)findViewById(R.id.anxiousbtn);
        anxious.setOnClickListener(this);
        sleep=(ImageView)findViewById(R.id.sleepbtn);
        sleep.setOnClickListener(this);
        panic=(ImageView)findViewById(R.id.panicbtn);
        panic.setOnClickListener(this);
        ocd=(ImageView)findViewById(R.id.ocdbtn);
        ocd.setOnClickListener(this);
        depressed=(ImageView)findViewById(R.id.depressedbtn);
        depressed.setOnClickListener(this);
        SharedPreferences pref = getSharedPreferences(
                "user_credentials", MODE_PRIVATE);
        String user=pref.getString("username","user");
        if(!user.equals("user"))
        {
            ViewRecommended();
        }
        else
            {
            LinearLayout RecWrap=findViewById(R.id.recommend_wrap);
            RecWrap.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View view) {
        int id=view.getId();

        if(id==R.id.afraidbtn)
        {
            String externalurl="http://10.0.2.2:8080/resource/list/Anxiety";
            launchExternalPage(externalurl,"Feeling Afraid?");
        }
        else if(id==R.id.anxiousbtn)
        {
            String externalurl="http://10.0.2.2:8080/resource/list/GAD";
            launchExternalPage(externalurl,"Tense and Anxious?");
        }
        else if(id==R.id.sleepbtn)
        {
            String externalurl="http://10.0.2.2:8080/resource/list/Sleep";
            launchExternalPage(externalurl,"Trouble Sleeping?");
        }
        else if(id==R.id.panicbtn)
        {
            String externalurl="http://10.0.2.2:8080/resource/list/Panic";
            launchExternalPage(externalurl,"Panicking about panic?");
        }
        else if(id==R.id.ocdbtn)
        {
            String externalurl="http://10.0.2.2:8080/resource/list/Ocd";
            launchExternalPage(externalurl,"Compulsion?");
        }
        else if(id==R.id.depressedbtn)
        {
            String externalurl="http://10.0.2.2:8080/resource/list/Depression";
            launchExternalPage(externalurl,"Feeling down?");
        }
      else if(id==R.id.carousel)
        {
            String externalUrl ="http://10.0.2.2:8080/resource/view/" +Url_code;
            launchExternalPage(externalUrl,title);
        }
        /*else if(id==R.id.recommended2)
        {
            String externalUrl ="http://10.0.2.2:8080/resource/view/" +Url_code;
            launchExternalPage(externalUrl,title);
        }*/
    }
    public void launchExternalPage(String externalurl,String title)
    {
        Intent intent=new Intent(Mindfulness.this, MindfulnessList.class);
        intent.putExtra("title",title);
        intent.putExtra(EXTERNAL_URL,externalurl);
        startActivity(intent);
    }
    public void ViewRecommended()
    {
        Intent intent=getIntent();
        String[] resource=intent.getStringArrayExtra(Resources.EXTERNAL_URL_1);//(Resources.EXTERNAL_URL_1);
        String title_1=intent.getStringExtra("title");
        String[] array= new String[4];
        for(int i =0;i<array.length;i++){
            array[i]=resource[new Random().nextInt(resource.length)-1];
        }
        CarouselView carousel=findViewById(R.id.carousel);
        carousel.setPageCount(array.length);
        carousel.setImageListener(new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                Picasso.get().load("https://img.youtube.com/vi/" + array[position] + "/0.jpg").placeholder(R.drawable.ic_launcher_background).into(imageView);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);

            }
        });
        carousel.setImageClickListener(new ImageClickListener() {
            @Override
            public void onClick(int position) {
                String externalUrl ="http://10.0.2.2:8080/resource/view/"+array[position];
                launchExternalPage(externalUrl,title_1);
            }
        });
    }

}