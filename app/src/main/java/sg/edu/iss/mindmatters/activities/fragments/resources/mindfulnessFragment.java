package sg.edu.iss.mindmatters.activities.fragments.resources;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import sg.edu.iss.mindmatters.R;
import sg.edu.iss.mindmatters.Results_temp;
import sg.edu.iss.mindmatters.activities.Mindfulness;
import sg.edu.iss.mindmatters.webview.MindfulnessList;

import static android.content.Context.MODE_PRIVATE;

public class mindfulnessFragment extends Fragment implements View.OnClickListener{

    LinearLayout afraid;
    LinearLayout anxious;
    LinearLayout sleep;
    LinearLayout panic;
    LinearLayout ocd;
    LinearLayout depressed;
    ImageView launchBtn;//
    ImageView launchBtn2;//
    String Url_code;
    String Url_code2;
    String title;

    public static final String EXTERNAL_URL="externalUrl";

    private String[] mindfulLinks;
    View mView;

    IMindfulnessFragment iMindfulnessFragment;

    public mindfulnessFragment() {
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
        View view = inflater.inflate(R.layout.fragment_mindfulness, container, false);
        this.mView = view;
        mindfulLinks = getArguments().getStringArray("resources");

        afraid=mView.findViewById(R.id.afraidbtn);
        afraid.setOnClickListener(this);
        anxious=mView.findViewById(R.id.anxiousbtn);
        anxious.setOnClickListener(this);
        sleep=mView.findViewById(R.id.sleepbtn);
        sleep.setOnClickListener(this);
        panic=mView.findViewById(R.id.panicbtn);
        panic.setOnClickListener(this);
        ocd=mView.findViewById(R.id.ocdbtn);
        ocd.setOnClickListener(this);
        depressed=mView.findViewById(R.id.depressedbtn);
        depressed.setOnClickListener(this);
        SharedPreferences pref = getActivity().getSharedPreferences(
                "user_credentials", MODE_PRIVATE);
        String user=pref.getString("username","user");
        if(!user.equals("user"))
        {
            ViewRecommended();
        }
        else
        {
            ConstraintLayout RecWrap = mView.findViewById(R.id.recommend_wrap);
            RecWrap.setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public void onClick(View view) {
        int id=view.getId();

        if(id==R.id.afraidbtn)
        {
            String externalurl="http://10.0.2.2:8080/resource/list/Anxiety";
            iMindfulnessFragment.mindfulClicked(launchExternalPage(externalurl,"Feeling Afraid?"));
        }
        else if(id==R.id.anxiousbtn)
        {
            String externalurl="http://10.0.2.2:8080/resource/list/GAD";
            iMindfulnessFragment.mindfulClicked(launchExternalPage(externalurl,"Tense and Anxious?"));
        }
        else if(id==R.id.sleepbtn)
        {
            String externalurl="http://10.0.2.2:8080/resource/list/Sleep";
            iMindfulnessFragment.mindfulClicked(launchExternalPage(externalurl,"Trouble Sleeping?"));
        }
        else if(id==R.id.panicbtn)
        {
            String externalurl="http://10.0.2.2:8080/resource/list/Panic";
            iMindfulnessFragment.mindfulClicked(launchExternalPage(externalurl,"Panicking about panic?"));
        }
        else if(id==R.id.ocdbtn)
        {
            String externalurl="http://10.0.2.2:8080/resource/list/Ocd";
            iMindfulnessFragment.mindfulClicked(launchExternalPage(externalurl,"Compelled by compulsion?"));
        }
        else if(id==R.id.depressedbtn)
        {
            String externalurl="http://10.0.2.2:8080/resource/list/Depression";
            iMindfulnessFragment.mindfulClicked(launchExternalPage(externalurl,"Feeling down?"));
        }
        else if(id==R.id.recommended1)
        {
            String externalUrl ="http://10.0.2.2:8080/resource/view/" +Url_code;
            iMindfulnessFragment.mindfulClicked(launchExternalPage(externalUrl,title));
        }
        else if(id==R.id.recommended2)
        {
            String externalUrl ="http://10.0.2.2:8080/resource/view/" +Url_code;
            iMindfulnessFragment.mindfulClicked(launchExternalPage(externalUrl,title));
        }
    }

    public String[] launchExternalPage(String externalurl,String title)
    {
//        Intent intent=new Intent(getActivity(), MindfulnessList.class);
//        intent.putExtra("title",title);
//        intent.putExtra(EXTERNAL_URL,externalurl);
//        startActivity(intent);

        return new String[]{externalurl, title};
    }
    public void ViewRecommended()
    {
//        Intent intent=getActivity().getIntent();
//        String url_1=intent.getStringExtra(Results_temp.EXTERNAL_URL_1);
//        Url_code=url_1.substring(27,url_1.length()-6);
//        title=intent.getStringExtra("title");
//        launchBtn = (ImageView)mView.findViewById(R.id.recommended1);
//        Picasso.get().load(url_1).placeholder(R.drawable.ic_launcher_background).into(launchBtn);
//        launchBtn.setOnClickListener(this);
//        String url_2=intent.getStringExtra(Results_temp.EXTERNAL_URL_2);
//        launchBtn2 =(ImageView)mView.findViewById(R.id.recommended2);
//        Picasso.get().load(url_2).placeholder(R.drawable.ic_launcher_background).into(launchBtn2);
//        Url_code2=url_2.substring(27,url_2.length()-6);
//        launchBtn2.setOnClickListener(this);

        String url_1=mindfulLinks[0];
        Url_code=url_1.substring(27,url_1.length()-6);
        title=mindfulLinks[2];
        launchBtn = (ImageView)mView.findViewById(R.id.recommended1);
        Picasso.get().load(url_1).placeholder(R.drawable.ic_launcher_background).into(launchBtn);
        launchBtn.setOnClickListener(this);
        String url_2=mindfulLinks[1];
        launchBtn2 =(ImageView)mView.findViewById(R.id.recommended2);
        Picasso.get().load(url_2).placeholder(R.drawable.ic_launcher_background).into(launchBtn2);
        Url_code2=url_2.substring(27,url_2.length()-6);
        launchBtn2.setOnClickListener(this);

    }

    public interface IMindfulnessFragment{
        void mindfulClicked(String[] content);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        iMindfulnessFragment = (IMindfulnessFragment) context;
    }

}