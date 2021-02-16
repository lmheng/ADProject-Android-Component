package sg.edu.iss.mindmatters.activities.fragments.resources;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;
import com.synnapps.carouselview.ViewListener;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sg.edu.iss.mindmatters.R;
import sg.edu.iss.mindmatters.RetrofitClient;
import sg.edu.iss.mindmatters.model.Resource;

import static android.content.Context.MODE_PRIVATE;

public class mindfulnessFragment extends Fragment implements View.OnClickListener{

    LinearLayout afraid;
    LinearLayout anxious;
    LinearLayout sleep;
    LinearLayout panic;
    LinearLayout ocd;
    LinearLayout depressed;
    CarouselView carousel;
    TextView caption;


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
    }

    public String[] launchExternalPage(String externalurl,String title)
    {
        return new String[]{externalurl, title};
    }


        public interface IMindfulnessFragment {
            void mindfulClicked(String[] content);
        }

        @Override
        public void onAttach (Context context){
            super.onAttach(context);
            iMindfulnessFragment = (IMindfulnessFragment) context;
        }
        public void ViewRecommended(){
            String[] resource = getArguments().getStringArray("recommend");//(Resources.EXTERNAL_URL_1);
            String[] array = new String[3];
            for (int i = 0; i < array.length; i++) {
                array[i] = resource[new Random().nextInt(resource.length-1)];
            }
            carousel=mView.findViewById(R.id.carousel);
            carousel.setPageCount(array.length);
            ViewListener viewListener = new ViewListener() {
                @Override
                public View setViewForPosition(int position) {
                    View customView = getLayoutInflater().inflate(R.layout.recommened_include, null);
                    ImageView imageView=customView.findViewById(R.id.carousel_img);
                    Picasso.get().load("https://img.youtube.com/vi/" + array[position] + "/0.jpg").placeholder(R.drawable.ic_launcher_background).into(imageView);
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    caption=customView.findViewById(R.id.carouseltext);
                    getTitleMapping(array[position],caption);
                    return customView;
                }
            };
            carousel.setViewListener(viewListener);
            /*carousel.setImageListener(new ImageListener() {
                @Override
                public void setImageForPosition(int position, ImageView imageView) {
                    Picasso.get().load("https://img.youtube.com/vi/" + array[position] + "/0.jpg").placeholder(R.drawable.ic_launcher_background).into(imageView);
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                }
            });*/
            carousel.setImageClickListener(new ImageClickListener() {
                @Override
                public void onClick(int position) {
                    String externalUrl = "http://10.0.2.2:8080/resource/view/" + array[position];
                    getResourceMapping(array[position],externalUrl);
                }
            });
        }
    public String getTitle(HashMap<String,String>map,String key)
    {
        String value=map.get(key);
        return value;
    }
    public void getResourceMapping(String key,String externalUrl)
    {
        Call<List<Resource>> call = RetrofitClient
                .getInstance()
                .getAPI()
                .getallresources();
        HashMap<String,String>map=new HashMap<>();
        call.enqueue(new Callback<List<Resource>>() {
            @Override
            public void onResponse(Call<List<Resource>> call, Response<List<Resource>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(getActivity(), "Can't get Resources", Toast.LENGTH_LONG).show();
                }

                List<Resource> allResources= response.body();
                for(Resource r:allResources)
                {
                    map.put(r.getUrlCode(),r.getName());
                }
                String value=getTitle(map,key);
               TextView caption=mView.findViewById(R.id.carouseltext);
               caption.setText(value);
               caption.setAutoSizeTextTypeUniformWithConfiguration(2,17,1, TypedValue.COMPLEX_UNIT_DIP);
               iMindfulnessFragment.mindfulClicked(launchExternalPage(externalUrl,value));
            }


            @Override
            public void onFailure(Call<List<Resource>> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }
    public void getTitleMapping(String key,TextView caption)
    {
        Call<List<Resource>> call = RetrofitClient
                .getInstance()
                .getAPI()
                .getallresources();
        HashMap<String,String>map=new HashMap<>();
        call.enqueue(new Callback<List<Resource>>() {
            @Override
            public void onResponse(Call<List<Resource>> call, Response<List<Resource>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(getActivity(), "Can't get Resources", Toast.LENGTH_LONG).show();
                }
                List<Resource> allResources= response.body();
                for(Resource r:allResources)
                {
                    map.put(r.getUrlCode(),r.getName());
                }
                String value=getTitle(map,key);
                caption.setText(value);
                caption.setAutoSizeTextTypeUniformWithConfiguration(2,17,1, TypedValue.COMPLEX_UNIT_DIP);
            }


            @Override
            public void onFailure(Call<List<Resource>> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }
}
