package sg.edu.iss.mindmatters.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;

import sg.edu.iss.mindmatters.MyApplication;
import sg.edu.iss.mindmatters.dao.SQLiteDatabaseHandler;
import sg.edu.iss.mindmatters.webview.QuizActivity;
import sg.edu.iss.mindmatters.R;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    SharedPreferences pref;
    SQLiteDatabaseHandler db = new SQLiteDatabaseHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pref = getSharedPreferences(
                "user_credentials", MODE_PRIVATE);

        runDailyQuiz(true);

        TextView tv = (TextView)findViewById(R.id.userNametv);
        tv.setText("Welcome, "+ pref.getString("username","user"));

        if(!pref.contains("token")){
            findViewById(R.id.next_date_taken).setVisibility(View.GONE);
            TextView header = findViewById(R.id.next_quiz_header);
            header.setText("Please log in if you want to view full details");
        }
        else{
            loadNextDate();
        }

        callCustomActionBar(MainActivity.this,true);
        MyApplication.setCurrentActivity("MainPage");
        findViewById(R.id.resource_btn).setOnClickListener(this);
        findViewById(R.id.test_button).setOnClickListener(this);
        findViewById(R.id.gethelp_button).setOnClickListener(this);
        findViewById(R.id.floatingActionButton).setOnClickListener(this);
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(pref.contains("token"))
            loadNextDate();
        runDailyQuiz(false);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.test_button){
            Intent intent = new Intent(this, QuizActivity.class);
            startActivity(intent);
        }
        else if(view.getId() == R.id.gethelp_button){
            Intent intent = new Intent(this, GetHelpList.class);
            startActivity(intent);
        }
        else if(view.getId() == R.id.resource_btn) {
            Intent intent = new Intent(this, Resources.class);
            startActivity(intent);
        }
        else if(view.getId() == R.id.floatingActionButton) {
            Intent intent = new Intent(this, DailyQuizActivity.class);
            startActivity(intent);
        }
    }

    public void runDailyQuiz(boolean startApp){
        try {
            if(startApp)
            {
                if (db.findDailyByDate(LocalDate.now(), pref.getString("username","user")) == null) {
                    Intent intent = new Intent(this, DailyQuizActivity.class);
                    startActivity(intent);
                }
                else{
                    findViewById(R.id.floatingActionButton).setVisibility(View.GONE);
                }
            }
            else{
                if (db.findDailyByDate(LocalDate.now(), pref.getString("username","user")) == null) {
                    findViewById(R.id.floatingActionButton).setVisibility(View.VISIBLE);
                }
                else{
                    findViewById(R.id.floatingActionButton).setVisibility(View.GONE);
                }
            }
        }
        catch(CursorIndexOutOfBoundsException e) {
            Intent intent = new Intent(this, DailyQuizActivity.class);
            startActivity(intent);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void loadNextDate(){
        SharedPreferences pref1 = getSharedPreferences("QuizActivity", Context.MODE_PRIVATE);
        String getNextDate = pref1.getString(pref.getString("username","user"), null);

        System.out.println("nextDate" + getNextDate);

        TextView nextDate = findViewById(R.id.next_date_taken);
        nextDate.setVisibility(View.VISIBLE);
        TextView header = findViewById(R.id.next_quiz_header);
        header.setText(R.string.next_quiz_header);

        if(getNextDate != null) {
            nextDate.setText(getNextDate);
        }
        else {
            Calendar today = Calendar.getInstance();
            today.set(Calendar.HOUR_OF_DAY, 0);
            DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            nextDate.setText(sdf.format(today.getTime()));
        }
    }



}