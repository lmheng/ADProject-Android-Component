package sg.edu.iss.mindmatters.activities.fragments.resources;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sg.edu.iss.mindmatters.R;
import sg.edu.iss.mindmatters.RetrofitClient;
import sg.edu.iss.mindmatters.activities.Mindfulness;
import sg.edu.iss.mindmatters.activities.Resources;
import sg.edu.iss.mindmatters.model.QuizOutcome;
import sg.edu.iss.mindmatters.model.Resource;
import sg.edu.iss.mindmatters.webview.Education;

import static android.content.Context.MODE_PRIVATE;

public class resourceFragment extends Fragment implements View.OnClickListener {

    LinearLayout mindfulness;
    LinearLayout education;
    public static final String EXTERNAL_URL_1="externalUrl1";
    public static final String EXTERNAL_URL_2="externalUrl2";
    public static final String EXTERNAL_EDU="externalurl";
    private String[]Anxiety= new String[]{};
    private String[]Sleep= new String[]{};
    private String[]GAD=new String[]{};
    private String[]OCD=new String[]{};
    private String[]Depression=new String[]{};
    private String[]Panic=new String[]{};
    private String[]Loneliness=new String[]{};
    private String[]Stress=new String[]{};
    private String[]All=new String[]{};
    List<Resource> collect=new ArrayList<>();
    SharedPreferences pref;
    String User="";
    String outcome="";

    View mView;

    public resourceFragment() {
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

        View view = inflater.inflate(R.layout.fragment_resource, container, false);
        this.mView = view;

        mindfulness=mView.findViewById(R.id.mindful_layout);
        mindfulness.setOnClickListener(this);
        education=mView.findViewById(R.id.education_layout);
        education.setOnClickListener(this);
        getResourceList();
        pref = getActivity().getSharedPreferences(
                "user_credentials", MODE_PRIVATE);
        User=pref.getString("username","user");
        if(!User.equals("user")){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if(getOutcome(User).toLowerCase().equals("normal"))
                        outcome = "all";
                    else
                        outcome=getOutcome(User).toLowerCase();
                }
            }).start();}
        else{
            outcome="all";
        }

        return view;
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
            new Thread(new Runnable() {
                @Override
                public void run() { recommendation(outcome);
                    System.out.println("thread running");
                }
            }).start();
        }
    }

    public void launchExternalPage(String externalurl)
    {
        Intent intent=new Intent(getActivity(), Education.class);
        intent.putExtra(EXTERNAL_EDU, externalurl);
        startActivity(intent);
    }

    public void launchExternalPage(String externalurl1,String externalurl2,String title)
    {
        Intent intent=new Intent(getActivity(), Mindfulness.class);
        intent.putExtra(EXTERNAL_URL_1,externalurl1);
        intent.putExtra(EXTERNAL_URL_2,externalurl2);
        intent.putExtra("title",title);
        startActivity(intent);
    }
    static int RandomNo(String[]arr)
    {
        Random rd=new Random();
        return rd.nextInt(arr.length-1);
    }

    public void recommendation(String outcome) {
        switch (outcome) {
            case "anxiety": {
                String externalurl1 = "https://img.youtube.com/vi/" + Anxiety[RandomNo(Anxiety)] + "/0.jpg";
                String externalurl2 = "https://img.youtube.com/vi/" + Anxiety[RandomNo(Anxiety)] + "/0.jpg";
                String title = "Feeling Afraid?";
                launchExternalPage(externalurl1, externalurl2, title);
                break;
            }
            case "depressed": {
                String externalurl1 = "https://img.youtube.com/vi/" + Depression[RandomNo(Depression)] + "/0.jpg";
                String externalurl2 = "https://img.youtube.com/vi/" + Depression[RandomNo(Depression)] + "/0.jpg";
                String title = "Feeling Down?";
                launchExternalPage(externalurl1, externalurl2, title);
                break;
            }
            case "gad": {
                String externalurl1 = "https://img.youtube.com/vi/" + GAD[RandomNo(GAD)] + "/0.jpg";
                String externalurl2 = "https://img.youtube.com/vi/" + GAD[RandomNo(GAD)] + "/0.jpg";
                String title = "Tense and Anxious?";
                launchExternalPage(externalurl1, externalurl2, title);
                break;
            }
            case "sleep": {
                String externalurl1 = "https://img.youtube.com/vi/" + Sleep[RandomNo(Sleep)] + "/0.jpg";
                String externalurl2 = "https://img.youtube.com/vi/" + Sleep[RandomNo(Sleep)] + "/0.jpg";
                String title = "Trouble Sleeping?";
                launchExternalPage(externalurl1, externalurl2, title);
                break;
            }
            case "ocd": {
                String externalurl1 = "https://img.youtube.com/vi/" + OCD[RandomNo(OCD)] + "/0.jpg";
                String externalurl2 = "https://img.youtube.com/vi/" + OCD[RandomNo(OCD)] + "/0.jpg";
                String title = "Compelled by compulsion?";
                launchExternalPage(externalurl1, externalurl2, title);
                break;
            }
            case "panic": {
                String externalurl1 = "https://img.youtube.com/vi/" + Panic[RandomNo(Panic)] + "/0.jpg";
                String externalurl2 = "https://img.youtube.com/vi/" + Panic[RandomNo(Panic)] + "/0.jpg";
                String title = "Panicking about panic";
                launchExternalPage(externalurl1, externalurl2, title);
                break;
            }
            case "all": {
                String externalurl1 = "https://img.youtube.com/vi/" + All[RandomNo(All)] + "/0.jpg";
                String externalurl2 = "https://img.youtube.com/vi/" + All[RandomNo(All)] + "/0.jpg";
                String title = "Panicking about panic";
                launchExternalPage(externalurl1, externalurl2, title);
                break;
            }
            case "stress": {
                String externalurl1 = "https://img.youtube.com/vi/" + Stress[RandomNo(Stress)] + "/0.jpg";
                String externalurl2 = "https://img.youtube.com/vi/" + Stress[RandomNo(Stress)] + "/0.jpg";
                String title = "Panicking about panic";
                launchExternalPage(externalurl1, externalurl2, title);
                break;
            }
            case "loneliness": {
                String externalurl1 = "https://img.youtube.com/vi/" + Loneliness[RandomNo(Loneliness)] + "/0.jpg";
                String externalurl2 = "https://img.youtube.com/vi/" + Loneliness[RandomNo(Loneliness)] + "/0.jpg";
                String title = "Panicking about panic";
                launchExternalPage(externalurl1, externalurl2, title);
                break;
            }
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
                    Toast.makeText(getActivity(), "Can't get Resources", Toast.LENGTH_LONG).show();
                }
                List<Resource> allResources= response.body();
                for(Resource r:allResources)
                {
                    System.out.println(r.getName());
                    collect.add(r);

                }
                SplitListByType();
            }

            @Override
            public void onFailure(Call<List<Resource>> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }
    public void SplitListByType()
    {

        Anxiety=collect.stream().filter(x->x.getType().equalsIgnoreCase("Anxiety")).map(x->x.getUrlCode()).toArray(size->new String[size]);
        Sleep=collect.stream().filter(x->x.getType().equalsIgnoreCase("Sleep")).map(x->x.getUrlCode()).toArray(String[]::new);
        OCD=collect.stream().filter(x->x.getType().equalsIgnoreCase("Ocd")).map(x->x.getUrlCode()).toArray(String[]::new);
        GAD=collect.stream().filter(x->x.getType().equalsIgnoreCase("Gad")).map(x->x.getUrlCode()).toArray(String[]::new);
        Panic=collect.stream().filter(x->x.getType().equalsIgnoreCase("Panic")).map(x->x.getUrlCode()).toArray(String[]::new);
        Depression=collect.stream().filter(x->x.getType().equalsIgnoreCase("Depression")).map(x->x.getUrlCode()).toArray(String[]::new);
        All=collect.stream().filter(x->!x.getType().equalsIgnoreCase("Education")).map(x->x.getUrlCode()).toArray(String[]::new);
        Loneliness=collect.stream().filter(x->x.getType().equalsIgnoreCase("Loneliness")).map(x->x.getUrlCode()).toArray(size->new String[size]);
        Stress=collect.stream().filter(x->x.getType().equalsIgnoreCase("Stress")).map(x->x.getUrlCode()).toArray(size->new String[size]);

    }

    public String getOutcome(String user){
       /* Gson gson=new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .create();
       Gson gson = new GsonBuilder().registerTypeAdapter(Calendar.class, new JsonDeserializer<Calendar>() {
            @Override
            public Calendar deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                    throws JsonParseException {
                return Calendar.getInstance(TimeZone.getTimeZone(json.getAsString()));
            }
        }).create();*/
       /* Retrofit retrofit= new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        API api =retrofit.create(API.class);

        Call<QuizOutcome> call = api.getUserProfile(user);*/


        Call<QuizOutcome> call = RetrofitClient
                .getInstance()
                .getAPI()
                .getUserProfile(user);

        try {
            Response<QuizOutcome> qo=call.execute();
            QuizOutcome oc= qo.body();
            return oc.getQuizOutcome();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }





        /*call.enqueue(new Callback<QuizOutcome>() {
            @Override
            public void onResponse(Call<QuizOutcome> call, Response<QuizOutcome> response) {
                System.out.println("response code:"+response.code());
                if(!response.isSuccessful()){
                    Toast.makeText(Resources.this, "Can't get quiz outcome", Toast.LENGTH_LONG).show();

                }
                else {
                    QuizOutcome oc = response.body();
                    outcome = oc.getQuizOutcome().toLowerCase();
                }
            }

            @Override
            public void onFailure(Call<QuizOutcome> call, Throwable t) {
                System.out.println("Jason failed to parse");
                Toast.makeText(Resources.this, "json failed to parse", Toast.LENGTH_LONG).show();
            }
        });*/
    }

}