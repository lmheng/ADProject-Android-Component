package sg.edu.iss.mindmatters;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Resources extends ParentActivity implements View.OnClickListener {

    RelativeLayout mindfulness;
    RelativeLayout education;
    public static final String EXTERNAL_URL_1="externalUrl1";
    public static final String EXTERNAL_URL_2="externalUrl2";
    public static final String EXTERNAL_EDU="externalurl";
    List<Resource> allResources=new ArrayList<>();
//    private String[]Anxiety= new String[]{"HT_ZvD94_kE", "Lysn2Zoio8Y", "SNqYG95j_UQ"};
//    private String[]Sleep= new String[]{"86HUcX8ZtAk", "hkyKHk7s13s", "TP2gb2fSYXY"};
//    private String[]GAD=new String[]{"kzzb3jHhgeU", "LqzbrcpdqR4"};
//    private String[]OCD=new String[]{"GcJLXqfG-PQ", "phm_VPjijh8"};
//    private String[]Depression=new String[]{"B-A4CzvHCLE", "O3Ku-cpdSJM"};
//    private String[]Panic=new String[]{"8vkYJf8DOsc", "sz-cNBAK7Qs"};
//    private String[]Outcomes=new String[]{"anxiety","sleep","gad","ocd","depression","panic"};
    private String[]Anxiety= new String[]{};
    private String[]Sleep= new String[]{};
    private String[]GAD=new String[]{};
    private String[]OCD=new String[]{};
    private String[]Depression=new String[]{};
    private String[]Panic=new String[]{};
    private String[]Outcomes=new String[]{};
    private String outcome="";
    SharedPreferences pref;
    String User;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resources);

        mindfulness=(RelativeLayout)findViewById(R.id.mindful_layout);
        mindfulness.setOnClickListener(this);
        education=(RelativeLayout)findViewById(R.id.education_layout);
        education.setOnClickListener(this);
        callCustomActionBar();
        getResourceList();;
        SplitListByType();
       pref = getSharedPreferences(
                "user_credentials", MODE_PRIVATE);
        User=pref.getString("username","user");

        if(User=="user"||User==null)
        {
            launchExternalPage();
        }
        getOutcome();


        //find the user's profile as string.lowercase
        //send intent over the next page with profile

    }

    @Override
    public void onClick(View view) {

        int id=view.getId();
        if(id==R.id.education_layout)
        {
            String externalurl="http://10.0.2.2:8080/resource/edulist/Education";
            launchExternalPage(externalurl);

        }
        else if(id==R.id.mindful_layout)
        {
           String user_outcome=Outcomes[RandomNo(Outcomes)];
            recommendation(user_outcome);
        }

    }
    public void launchExternalPage()
    {
        Intent intent=new Intent(Resources.this, Mindfulness.class);
        startActivity(intent);
    }
    public void launchExternalPage(String externalurl)
    {
        Intent intent=new Intent(Resources.this, Education.class);
        intent.putExtra(EXTERNAL_EDU, externalurl);
        startActivity(intent);
    }
    public void launchExternalPage(String externalurl1,String externalurl2,String title)
    {
        Intent intent=new Intent(Resources.this, Mindfulness.class);
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

    public void recommendation(String outcome) {
        if (outcome == "anxiety") {
            String externalurl1 = "https://img.youtube.com/vi/" + Anxiety[RandomNo(Anxiety)] + "/0.jpg";
            String externalurl2 = "https://img.youtube.com/vi/" + Anxiety[RandomNo(Anxiety)] + "/0.jpg";
            String title = "Feeling Afraid?";
            launchExternalPage(externalurl1, externalurl2, title);
        } else if (outcome == "depressed") {
            String externalurl1 = "https://img.youtube.com/vi/" + Depression[RandomNo(Depression)] + "/0.jpg";
            String externalurl2 = "https://img.youtube.com/vi/" + Depression[RandomNo(Depression)] + "/0.jpg";
            String title = "Feeling Down?";
            launchExternalPage(externalurl1, externalurl2, title);
        } else if (outcome == "gad") {
            String externalurl1 = "https://img.youtube.com/vi/" + GAD[RandomNo(GAD)] + "/0.jpg";
            String externalurl2 = "https://img.youtube.com/vi/" + GAD[RandomNo(GAD)] + "/0.jpg";
            String title = "Tense and Anxious?";
            launchExternalPage(externalurl1, externalurl2, title);
        } else if (outcome == "sleep") {
            String externalurl1 = "https://img.youtube.com/vi/" + Sleep[RandomNo(Sleep)] + "/0.jpg";
            String externalurl2 = "https://img.youtube.com/vi/" + Sleep[RandomNo(Sleep)] + "/0.jpg";
            String title = "Trouble Sleeping?";
            launchExternalPage(externalurl1, externalurl2, title);
        } else if (outcome == "ocd") {
            String externalurl1 = "https://img.youtube.com/vi/" + OCD[RandomNo(OCD)] + "/0.jpg";
            String externalurl2 = "https://img.youtube.com/vi/" + OCD[RandomNo(OCD)] + "/0.jpg";
            String title = "Compelled by compulsion?";
            launchExternalPage(externalurl1, externalurl2, title);
        } else if (outcome == "panic") {
            String externalurl1 = "https://img.youtube.com/vi/" + Panic[RandomNo(Panic)] + "/0.jpg";
            String externalurl2 = "https://img.youtube.com/vi/" + Panic[RandomNo(Panic)] + "/0.jpg";
            String title = "Panicking about panic";
            launchExternalPage(externalurl1, externalurl2, title);
        }
    }

    public void getResourceList()
    {
        Call<List<Resource>> call = RetrofitClient
                .getInstance()
                .getAPI()
                .getallresources();
        call.enqueue(new Callback<List<Resource>>() {
            @Override
            public void onResponse(Call<List<Resource>> call, Response<List<Resource>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(Resources.this, "Can't get Resources", Toast.LENGTH_LONG).show();

                }
                allResources=response.body();
                for(Resource r:allResources)
                {
                    String content="";
                    content+="ID:" + r.getId()+"\n";
                    content+="Name:" + r.getName()+"\n";
                    content+="Description:" + r.getDescription()+"\n";
                    content+="Type:" + r.getType()+"\n";
                    content+="UrlCode:" + r.getUrlCode()+"\n\n";
                }
            }

            @Override
            public void onFailure(Call<List<Resource>> call, Throwable t) {
                Toast.makeText(Resources.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    public void getOutcome()
    {
        Call<QuizOutcome> call = RetrofitClient
                .getInstance()
                .getAPI()
                .getUserProfile(User);
        call.enqueue(new Callback<QuizOutcome>() {
            @Override
            public void onResponse(Call<QuizOutcome> call, Response<QuizOutcome> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(Resources.this, "Can't get quiz outcome", Toast.LENGTH_LONG).show();

                }
                outcome=response.body().getQuizOutcome();

            }

            @Override
            public void onFailure(Call<QuizOutcome> call, Throwable t) {
                Toast.makeText(Resources.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    public void SplitListByType()
    {
        Anxiety=allResources.stream().filter(x->x.getType()=="Anxiety").map(x->x.getUrlCode()).toArray(String[]::new);
        Sleep=allResources.stream().filter(x->x.getType()=="Sleep").map(x->x.getUrlCode()).toArray(String[]::new);
        OCD=allResources.stream().filter(x->x.getType()=="Ocd").map(x->x.getUrlCode()).toArray(String[]::new);
        GAD=allResources.stream().filter(x->x.getType()=="Gad").map(x->x.getUrlCode()).toArray(String[]::new);
        Panic=allResources.stream().filter(x->x.getType()=="Panic").map(x->x.getUrlCode()).toArray(String[]::new);
        Depression=allResources.stream().filter(x->x.getType()=="Depression").map(x->x.getUrlCode()).toArray(String[]::new);

    }
}