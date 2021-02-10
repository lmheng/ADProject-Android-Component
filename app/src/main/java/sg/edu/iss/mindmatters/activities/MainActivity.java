package sg.edu.iss.mindmatters.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Response;
import sg.edu.iss.mindmatters.MyApplication;
import sg.edu.iss.mindmatters.RetrofitClient;
import sg.edu.iss.mindmatters.dao.SQLiteDatabaseHandler;
import sg.edu.iss.mindmatters.model.QuizOutcome;
import sg.edu.iss.mindmatters.webview.QuizActivity;
import sg.edu.iss.mindmatters.R;

public class MainActivity extends BaseActivity implements View.OnClickListener {


   public SharedPreferences pref;
    private LineChart linechart;
    SQLiteDatabaseHandler db;
    String user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*TextView profiletype=findViewById(R.id.currentStatus);
        TextView averagesleep=findViewById(R.id.averagesleep);
        TextView averagemood=findViewById(R.id.averagemood);
        new Thread(new Runnable() {
            @Override
            public void run() {
                String outcome=getOutcome("justin");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        profiletype.setText(outcome);
                    }
                });
            }
        }).start();*/


        pref = getSharedPreferences(
                "user_credentials", MODE_PRIVATE);
        TextView tv = (TextView)findViewById(R.id.userNametv);
       user=pref.getString("username","user");
        tv.setText("Welcome, "+user);

        if(!pref.contains("token")){
            findViewById(R.id.next_date_taken).setVisibility(View.GONE);
            TextView header = findViewById(R.id.next_quiz_header);
            header.setText("Please log in if you want to view full details");
            LinearLayout landing=findViewById(R.id.landingscreen);
            landing.setBackground(getDrawable(R.drawable.background));
        }
        else{
            loadNextDate();
            LinearLayout dashView=findViewById(R.id.dash_support);
            dashView.setVisibility(View.VISIBLE);
            populateDash(user);
            populateGraph();
        }
        /*db=new SQLiteDatabaseHandler(this);
        linechart=(LineChart)findViewById(R.id.linechart);
        linechart.setDragEnabled(true);
        linechart.setScaleEnabled(false);
        ArrayList<Entry>yValues=new ArrayList<>();
        ArrayList<Entry>xValues=new ArrayList<>();
        yValues=db.getMoodData(user);
        xValues=db.getSleepData(user);
       String sleepquality=db.getSleepQualityData(user);
       float avgMood=db.averageMoodData(user);
       averagemood.setText(String.format("%.1f",avgMood/10));
       float avgSlp=db.averageSleepData(user);
        averagesleep.setText(String.format("%.1f",avgSlp));
        LineDataSet set2=new LineDataSet(xValues,"Mood");
        LineDataSet set1=new LineDataSet(yValues,"Sleep");
        set1.setFillAlpha(110);
        set1.setFillColor(android.R.color.holo_blue_bright);
        set1.setLineWidth(3f);
        set1.setDrawFilled(true);
        set2.setFillAlpha(85);
        set2.setFillColor(android.R.color.black);
        set2.setLineWidth(3f);
        set2.setDrawFilled(true);
        ArrayList<ILineDataSet>datasets=new ArrayList<>();
        datasets.add(set1);
        datasets.add(set2);
        LineData data = new LineData(datasets);
        linechart.setData(data);
        linechart.getAxisRight().setEnabled(false);
        linechart.setDrawGridBackground(false);
        linechart.getAxisRight().setDrawGridLines(false);
        linechart.getAxisLeft().setDrawGridLines(false);
        linechart.getXAxis().setDrawGridLines(false);*/
        callCustomActionBar(MainActivity.this,true);
        MyApplication.setCurrentActivity("MainPage");
        findViewById(R.id.resource_btn).setOnClickListener(this);
        findViewById(R.id.test_button).setOnClickListener(this);
        findViewById(R.id.gethelp_button).setOnClickListener(this);
        findViewById(R.id.dailyquiz_button).setOnClickListener(this);
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(pref.contains("token"))
            loadNextDate();
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
        else if(view.getId() == R.id.dailyquiz_button) {
            Intent intent = new Intent(this, DailyQuizActivity.class);
            startActivity(intent);
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



public void populateGraph()
{
    db=new SQLiteDatabaseHandler(this);
    linechart=(LineChart)findViewById(R.id.linechart);
    linechart.setDragEnabled(true);
    linechart.setScaleEnabled(false);
    ArrayList<Entry>yValues=new ArrayList<>();
    ArrayList<Entry>xValues=new ArrayList<>();
    yValues=db.getMoodData(user);
    xValues=db.getSleepData(user);
    String sleepquality=db.getSleepQualityData(user);
    LineDataSet set2=new LineDataSet(xValues,"Mood");
    LineDataSet set1=new LineDataSet(yValues,"Sleep");
    set1.setFillAlpha(110);
    set1.setFillColor(android.R.color.holo_blue_bright);
    set1.setLineWidth(3f);
    set1.setDrawFilled(true);
    set2.setFillAlpha(85);
    set2.setFillColor(android.R.color.black);
    set2.setLineWidth(3f);
    set2.setDrawFilled(true);
    ArrayList<ILineDataSet>datasets=new ArrayList<>();
    datasets.add(set1);
    datasets.add(set2);
    LineData data = new LineData(datasets);
    linechart.setData(data);
    linechart.getAxisRight().setEnabled(false);
    linechart.setDrawGridBackground(false);
    linechart.getAxisRight().setDrawGridLines(false);
    linechart.getAxisLeft().setDrawGridLines(false);
    linechart.getXAxis().setDrawGridLines(false);

}
public void populateDash(String user){
    TextView profiletype=findViewById(R.id.currentStatus);
    TextView averagesleep=findViewById(R.id.averagesleep);
    TextView averagemood=findViewById(R.id.averagemood);
    db=new SQLiteDatabaseHandler(this);
    float avgMood=db.averageMoodData(user);
    averagemood.setText(String.format("%.1f",avgMood/10));
    float avgSlp=db.averageSleepData(user);
    averagesleep.setText(String.format("%.1f",avgSlp));
    new Thread(new Runnable() {
        @Override
        public void run() {
            String outcome=getOutcome(user);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    profiletype.setText(outcome);
                }
            });
        }
    }).start();
}



}