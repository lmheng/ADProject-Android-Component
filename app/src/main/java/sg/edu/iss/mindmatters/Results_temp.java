package sg.edu.iss.mindmatters;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Random;

import sg.edu.iss.mindmatters.activities.BaseActivity;
import sg.edu.iss.mindmatters.activities.Mindfulness;

public class Results_temp extends BaseActivity
        implements View.OnClickListener  {
public static final String EXTERNAL_URL_1="externalUrl1";
public static final String EXTERNAL_URL_2="externalUrl2";
private String[]Anxiety= new String[]{"HT_ZvD94_kE", "Lysn2Zoio8Y", "SNqYG95j_UQ"};
private String[]Sleep= new String[]{"86HUcX8ZtAk", "hkyKHk7s13s", "TP2gb2fSYXY"};
private String[]GAD=new String[]{"kzzb3jHhgeU", "LqzbrcpdqR4"};
private String[]OCD=new String[]{"GcJLXqfG-PQ", "phm_VPjijh8"};
private String[]Depression=new String[]{"B-A4CzvHCLE", "O3Ku-cpdSJM"};
private String[]Panic=new String[]{"8vkYJf8DOsc", "sz-cNBAK7Qs"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        callCustomActionBar(Results_temp.this, true);

        Button btn1 = findViewById(R.id.anxiety);
        if (btn1 != null)
            btn1.setOnClickListener(this);

        Button btn2 = findViewById(R.id.depressed);
        if (btn2 != null)
            btn2.setOnClickListener(this);

        Button btn3 = findViewById(R.id.GAD);
        if (btn3 != null)
            btn3.setOnClickListener(this);

        Button btn4 = findViewById(R.id.sleep);
        if (btn4 != null)
            btn4.setOnClickListener(this);
        Button btn5 = findViewById(R.id.ocd);
        if (btn5 != null)
            btn5.setOnClickListener(this);
        Button btn6 = findViewById(R.id.panic);
        if (btn6 != null)
            btn6.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.anxiety) {
            String externalurl1="https://img.youtube.com/vi/"+Anxiety[RandomNo(Anxiety)]+"/0.jpg";
            String externalurl2="https://img.youtube.com/vi/"+Anxiety[RandomNo(Anxiety)]+"/0.jpg";
            String title="Feeling Afraid?";
            launchExternalPage(externalurl1,externalurl2,title);
        }
        else if (id == R.id.depressed) {
            String externalurl1="https://img.youtube.com/vi/"+Depression[RandomNo(Depression)]+"/0.jpg";
            String externalurl2="https://img.youtube.com/vi/"+Depression[RandomNo(Depression)]+"/0.jpg";
            String title="Feeling Down?";
            launchExternalPage(externalurl1,externalurl2,title);
        }
        else if (id == R.id.GAD) {
            String externalurl1="https://img.youtube.com/vi/"+GAD[RandomNo(GAD)]+"/0.jpg";
            String externalurl2="https://img.youtube.com/vi/"+GAD[RandomNo(GAD)]+"/0.jpg";
            String title="Tense and Anxious?";
            launchExternalPage(externalurl1,externalurl2,title);
        }
        else if (id == R.id.sleep) {
            String externalurl1="https://img.youtube.com/vi/"+Sleep[RandomNo(Sleep)]+"/0.jpg";
            String externalurl2="https://img.youtube.com/vi/"+Sleep[RandomNo(Sleep)]+"/0.jpg";
            String title="Trouble Sleeping?";
            launchExternalPage(externalurl1,externalurl2,title);
        }
        else if (id == R.id.ocd) {
            String externalurl1="https://img.youtube.com/vi/"+OCD[RandomNo(OCD)]+"/0.jpg";
            String externalurl2="https://img.youtube.com/vi/"+OCD[RandomNo(OCD)]+"/0.jpg";
            String title="Compelled by compulsion?";
            launchExternalPage(externalurl1,externalurl2,title);
        }
        else if (id == R.id.panic) {
            String externalurl1="https://img.youtube.com/vi/"+Panic[RandomNo(Panic)]+"/0.jpg";
            String externalurl2="https://img.youtube.com/vi/"+Panic[RandomNo(Panic)]+"/0.jpg";
            String title="Panicking about panic";
            launchExternalPage(externalurl1,externalurl2,title);
        }
    }

    public void launchExternalPage(String externalurl1,String externalurl2,String title)
    {
        Intent intent=new Intent(Results_temp.this, Mindfulness.class);
        intent.putExtra(EXTERNAL_URL_1,externalurl1);
        intent.putExtra(EXTERNAL_URL_2,externalurl2);
        intent.putExtra("title",title);
        startActivity(intent);
    }

    static int RandomNo(String[]arr)
    {
        Random rd=new Random();
        return rd.nextInt(arr.length);
    }
}