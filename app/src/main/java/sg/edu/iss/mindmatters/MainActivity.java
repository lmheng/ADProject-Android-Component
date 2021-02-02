package sg.edu.iss.mindmatters;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pref = getSharedPreferences(
                "user_credentials", MODE_PRIVATE);
        TextView tv = (TextView)findViewById(R.id.userNametv);
        tv.setText("Welcome, "+pref.getString("username","user"));

        callCustomActionBar(MainActivity.this,true);
        loadNextDate();
        MyApplication.setCurrentActivity("MainPage");

        findViewById(R.id.test_button).setOnClickListener(this);
        findViewById(R.id.gethelp_button).setOnClickListener(this);
    }

    @Override
    protected void onResume(){
        super.onResume();
        loadNextDate();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.test_button){
            Intent intent = new Intent(this, QuizActivity.class);
            startActivity(intent);
        }
        else if(view.getId() == R.id.gethelp_button){
            Intent intent = new Intent(this, GetHelp.class);
            startActivity(intent);
        }
    }

    public void loadNextDate(){
        SharedPreferences pref = getSharedPreferences("QuizActivity", Context.MODE_PRIVATE);
        String getNextDate = pref.getString("nextDate", null);

        TextView nextDate = findViewById(R.id.next_date);
        System.out.println("getNextDate data" + getNextDate);

        if(getNextDate != null) {
            nextDate.setText(getNextDate);
        }
        else {
            Calendar today = Calendar.getInstance();
            today.set(Calendar.HOUR_OF_DAY, 0);
            DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

            nextDate.setText(sdf.format(today.getTime()));
        }
    }
}