package sg.edu.iss.mindmatters.activities.fragments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.time.LocalDate;

import sg.edu.iss.mindmatters.MyApplication;
import sg.edu.iss.mindmatters.R;
import sg.edu.iss.mindmatters.activities.Alarms;
import sg.edu.iss.mindmatters.activities.BaseActivity;
import sg.edu.iss.mindmatters.activities.DailyQuizActivity;
import sg.edu.iss.mindmatters.activities.SettingsActivity;
import sg.edu.iss.mindmatters.activities.fragments.getHelp.getHelpFragment;
import sg.edu.iss.mindmatters.activities.fragments.landing.LandingFragment;
import sg.edu.iss.mindmatters.activities.fragments.nonLogged.NonLoggedUsers;
import sg.edu.iss.mindmatters.activities.fragments.quiz.quizFragment;
import sg.edu.iss.mindmatters.activities.fragments.resources.educationFragment;
import sg.edu.iss.mindmatters.activities.fragments.resources.mindfulDetailFragment;
import sg.edu.iss.mindmatters.activities.fragments.resources.mindfulnessFragment;
import sg.edu.iss.mindmatters.activities.fragments.resources.resourceFragment;
import sg.edu.iss.mindmatters.dao.SQLiteDatabaseHandler;

public class LandingActivity extends BaseActivity implements View.OnClickListener, NonLoggedUsers.INonLoggedUsersFragment, resourceFragment.IResourceFragment, mindfulnessFragment.IMindfulnessFragment{

    final Fragment[] fragments = {new resourceFragment(), new quizFragment(), new getHelpFragment(), new LandingFragment(), new NonLoggedUsers(), new mindfulnessFragment(), new educationFragment(), new mindfulDetailFragment()};
    final int[] views = {R.id.resources, R.id.take_quiz, R.id.get_help, R.id.home_btn, R.id.home_btn};
    BottomNavigationView nav;

    SharedPreferences pref;
    SQLiteDatabaseHandler db = new SQLiteDatabaseHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        callCustomActionBar(this,false);
        MyApplication.setCurrentActivity("Landing Activity");
        loadBottomNavigation();

        pref = getSharedPreferences("user_credentials", MODE_PRIVATE);
        createNotificationChannel();

        if(savedInstanceState == null)
        {if(pref.contains("token"))
            {
                launchAlarm();
//                db.createDummyData(pref.getString("username", "user"));

                try {
                    if (db.findDailyByDate(LocalDate.now(), pref.getString("username","user")) == null)
                    {
                        Intent intent = new Intent(this, DailyQuizActivity.class);
                        startActivity(intent);
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }

                replaceDetailFragment(3, true);
            }
            else{
                replaceDetailFragment(4, true);
            }}

        findViewById(R.id.resources).setOnClickListener(this);
        findViewById(R.id.home_btn).setOnClickListener(this);
        findViewById(R.id.take_quiz).setOnClickListener(this);
        findViewById(R.id.get_help).setOnClickListener(this);
        findViewById(R.id.settings).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.home_btn){
            if(pref.contains("token"))
                replaceDetailFragment(3, false);
            else
                replaceDetailFragment(4, false);
        }
        else if(view.getId() == R.id.resources) {
            replaceDetailFragment(0, false);
        }
        else if(view.getId() == R.id.take_quiz) {
            replaceDetailFragment(1, false);
        }
        else if(view.getId() == R.id.get_help) {
            replaceDetailFragment(2, false);
        }
        else if(view.getId() == R.id.settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.right_to_left_enter,R.anim.right_to_left_exit);
        }
    }

    public void replaceDetailFragment(int newItemId, String[] resources) {

        Fragment fragment = fragments[newItemId];

            Bundle arguments = new Bundle();
            arguments.putStringArray("resources", resources);
            fragment.setArguments(arguments);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction trans = fm.beginTransaction();
        trans.replace(R.id.fragment_container, fragment);
        trans.addToBackStack(null);
        trans.commit();

        nav.setSelectedItemId(views[0]);
    }

    public void replaceDetailFragment(int newItemId, boolean firstActivity) {

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction trans = fm.beginTransaction();
        trans.replace(R.id.fragment_container, fragments[newItemId], String.valueOf(newItemId));
        if(!firstActivity)
            trans.addToBackStack(String.valueOf(newItemId));
        trans.commit();

        if(newItemId < 5)
            nav.setSelectedItemId(views[newItemId]);
        else
            nav.setSelectedItemId(views[0]);
    }

    public void loadBottomNavigation(){
        nav = findViewById(R.id.navigation);
        nav.setSelectedItemId(R.id.home_btn);
    }

    @Override
    public void itemClicked(int content) {
        replaceDetailFragment(content, false);
    }

    @Override
    public void resourceClicked(String[] content) {
        if(content.length != 1){
            replaceDetailFragment(5, content);
        }
        else{
            replaceDetailFragment(6, content);
        }
    }

    @Override
    public void mindfulClicked(String[] content) {
        replaceDetailFragment(7, content);
    }

    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O);
        int importance = NotificationManager.IMPORTANCE_DEFAULT;

        NotificationChannel channel = new NotificationChannel("888888", "Message Notification Channel", importance);
        channel.setDescription("This channel is for displaying messages");

        NotificationManager notifMgr = getSystemService(NotificationManager.class);
        notifMgr.createNotificationChannel(channel);
    }

    public void launchAlarm(){
        try{
            if(getSharedPreferences("Settings", MODE_PRIVATE).getString("alarm", "not set").contains("not set")){
                Alarms.startAction(this);
            };
            if(getSharedPreferences("Settings", MODE_PRIVATE).getString("value", "not set").contains("not set")){
                Alarms.dailyTips(this);
            };
        }
        catch(NullPointerException e){
            Alarms.startAction(this);
            Alarms.dailyTips(this);
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();

        try {
            String tag = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName();
            if (tag != null) {
                if (Integer.parseInt(tag) < 5 && Integer.parseInt(tag) > 0)
                    nav.setSelectedItemId(views[Integer.parseInt(tag)]);
                else
                    nav.setSelectedItemId(views[0]);
            }
        }
        catch(Exception e){
            nav.setSelectedItemId(views[3]);
            e.printStackTrace();
        }
    }

}