package sg.edu.iss.mindmatters.activities.fragments.resources;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import sg.edu.iss.mindmatters.model.QuizOutcome;
import sg.edu.iss.mindmatters.model.Resource;

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

    IResourceFragment iResourceFragment;
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
                    if(getOutcome(User)==null)
                        outcome="all";
                    else if(getOutcome(User).toLowerCase().equals("normal"))
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

    public interface IResourceFragment{
        void resourceClicked(String[] content);
    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        if(id==R.id.education_layout)
        {
            String externalurl="http://10.0.2.2:8080/resource/edulist/Education";
            iResourceFragment.resourceClicked(launchExternalPage(externalurl));
        }
        else if(id==R.id.mindful_layout)
        {
           /*new Thread(new Runnable() {
               @Override
               public void run() { recommendation(outcome);
              }
            }).start();*/
            //iResourceFragment.resourceClicked(recommendation(outcome));
            Bundle bundle=new Bundle();
            bundle.putStringArray("recommend",recommendation(outcome));
            mindfulnessFragment fragment = new mindfulnessFragment();
            fragment.setArguments(bundle);
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.resourcefrag, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }
    public String[] launchExternalPage(String[] externalurl1)
    {
        String[] mindfulPages = externalurl1;
        return mindfulPages;
    }

    public String[] launchExternalPage(String externalurl)
    {
//        Intent intent=new Intent(getActivity(), Education.class);
//        intent.putExtra(EXTERNAL_EDU, externalurl);
//        startActivity(intent);

        String[] educationSource = {externalurl};
        return educationSource;
    }

    /*public String[] launchExternalPage(String externalurl1, String externalurl2, String title)
    {
        Intent intent=new Intent(getActivity(), Mindfulness.class);
        intent.putExtra(EXTERNAL_URL_1, externalurl1);
        intent.putExtra(EXTERNAL_URL_2, externalurl2);
        intent.putExtra("title", title);
        startActivity(intent);
      //String[] mindfulPages = {externalurl1, externalurl2, title};
        return mindfulPages;
    }*/

    static int RandomNo(String[]arr)
    {
        Random rd=new Random();
        return rd.nextInt(arr.length-1);
    }

    public String[] recommendation(String outcome) {
        switch (outcome) {
            case "anxiety": {
                String[] externalurl1 = Anxiety;
                String title = "Feeling Afraid?";
                return launchExternalPage(externalurl1);

            }
            case "depressed": {
                String[] externalurl1 = Depression;
                String title = "Feeling Afraid?";
                return launchExternalPage(externalurl1);

            }
            case "gad": {
                String[] externalurl1 = GAD;
                String title = "Feeling Afraid?";
                return launchExternalPage(externalurl1);

            }
            case "sleep": {
                String[] externalurl1 = Sleep;
                String title = "Feeling Afraid?";
                return launchExternalPage(externalurl1);

            }
            case "ocd": {
                String[] externalurl1 = OCD;
                String title = "Feeling Afraid?";
                return launchExternalPage(externalurl1);

            }
            case "panic": {
                String[] externalurl1 = Panic;
                String title = "Feeling Afraid?";
                return launchExternalPage(externalurl1);

            }
            case "all": {
                String[] externalurl1 = All;
                String title = "Feeling Afraid?";
                return launchExternalPage(externalurl1);

            }
            case "stress": {
                String[] externalurl1 = Stress;
                String title = "Feeling Afraid?";
                return launchExternalPage(externalurl1);

            }
            case "loneliness": {
                String[] externalurl1 = Loneliness;
                String title = "Feeling Afraid?";
                return launchExternalPage(externalurl1);

            }

        }return null;
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

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        iResourceFragment = (IResourceFragment) context;
    }

}