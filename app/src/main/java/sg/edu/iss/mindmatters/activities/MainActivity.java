package sg.edu.iss.mindmatters.activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.io.EOFException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Response;
import sg.edu.iss.mindmatters.MyApplication;
import sg.edu.iss.mindmatters.RetrofitClient;
import sg.edu.iss.mindmatters.activities.fragments.LandingActivity;
import sg.edu.iss.mindmatters.dao.SQLiteDatabaseHandler;
import sg.edu.iss.mindmatters.model.QuizOutcome;
import sg.edu.iss.mindmatters.webview.QuizActivity;
import sg.edu.iss.mindmatters.R;

public class MainActivity extends BaseActivity implements View.OnClickListener {


   public SharedPreferences pref;
    SQLiteDatabaseHandler db = new SQLiteDatabaseHandler(this);
    String user;

    int DAILY_DONE = 1;
    boolean RESPONSE_CODE = true;

    protected BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("date_update")) {
                if(pref.contains("token"))
                    loadNextDate();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pref = getSharedPreferences(
                "user_credentials", MODE_PRIVATE);

        IntentFilter filter = new IntentFilter();
        filter.addAction("date_update");
        registerReceiver(receiver, filter);
        user=pref.getString("username","user");
        db=new SQLiteDatabaseHandler(this);
   //     db.createDummyData(user);
        TextView tv = (TextView)findViewById(R.id.userNametv);
        tv.setText("Welcome, "+ pref.getString("username","user"));
        if(!pref.contains("token")){
            findViewById(R.id.next_date_taken).setVisibility(View.GONE);
            findViewById(R.id.floatingActionButton).setVisibility(View.GONE);
            TextView header = findViewById(R.id.next_quiz_header);
            header.setText("Please log in if you want to view full details");
            LinearLayout landing=findViewById(R.id.landingscreen);
            landing.setBackground(getDrawable(R.drawable.background));
        }
        else{
            if(db.countDb()>0){
            LinearLayout dashView=findViewById(R.id.dash_support);
            dashView.setVisibility(View.VISIBLE);
            populateDash(user);
            combineGraph();}
            createNotificationChannel();
            runDailyQuiz(true);
            loadNextDate();
            launchAlarm();
            findViewById(R.id.floatingActionButton).setOnClickListener(this);
        }

        callCustomActionBar(MainActivity.this,true);
        MyApplication.setCurrentActivity("MainPage");
        findViewById(R.id.resource_btn).setOnClickListener(this);
        findViewById(R.id.test_button).setOnClickListener(this);
        findViewById(R.id.gethelp_button).setOnClickListener(this);
    }

    @Override
    protected void onResume(){
        super.onResume();
      if(pref.contains("token")) {
          runDailyQuiz(false);
      }
        if(db.countDb()>0){
            pref = getSharedPreferences(
                    "user_credentials", MODE_PRIVATE);
            user=pref.getString("username","user");
            populateDash(user);
            combineGraph();
        }
      }


    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.test_button){
            Intent intent = new Intent(this, QuizActivity.class);
            startActivity(intent);
        }
        else if(view.getId() == R.id.gethelp_button){
            Intent intent = new Intent(this, LandingActivity.class);
            startActivity(intent);
        }
        else if(view.getId() == R.id.resource_btn) {
            Intent intent = new Intent(this, Resources.class);
            startActivity(intent);
        }
        else if(view.getId() == R.id.floatingActionButton) {
            Intent intent = new Intent(this, DailyQuizActivity.class);
            startActivityForResult(intent, DAILY_DONE);
        }
    }

    public void runDailyQuiz(boolean startApp){
        try {
                if(startApp && RESPONSE_CODE)
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
            e.printStackTrace();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if(requestCode == DAILY_DONE) {
            if (resultCode == RESULT_CANCELED) {
                RESPONSE_CODE = intent.getBooleanExtra("action", false);
            }
            if (resultCode == RESULT_OK) {
                RESPONSE_CODE = intent.getBooleanExtra("action", true);
            }
        }
    }

    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O);
        int importance = NotificationManager.IMPORTANCE_DEFAULT;

        NotificationChannel channel = new NotificationChannel("123456", "Notif Channel", importance);
        channel.setDescription("To display messages");

        NotificationManager notifMgr = getSystemService(NotificationManager.class);
        notifMgr.createNotificationChannel(channel);
    }

    public void loadNextDate(){
        SharedPreferences pref1 = getSharedPreferences("webview.QuizActivity", Context.MODE_PRIVATE);
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
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            nextDate.setText(LocalDate.now().format(dtf));
        }
    }

    public void launchAlarm(){
        try{
            if(getSharedPreferences("Settings", MODE_PRIVATE).getString("alarm", "not set").contains("not set")){
                Alarms.startAction(this);
            };
        }
        catch(NullPointerException e){
            Alarms.startAction(this);
            e.printStackTrace();
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
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }



public LineData GenerateLine()
{
    db=new SQLiteDatabaseHandler(this);
    LineData line = new LineData();
    ArrayList<Entry>yValues=new ArrayList<>();
    yValues=db.getMoodData(user);
    LineDataSet set1=new LineDataSet(yValues,"Sleep");
    set1.setFillAlpha(android.R.color.holo_blue_bright);
    set1.setValueTextSize(10f);
    set1.setLineWidth(2f);
    line.addDataSet(set1);


    return line;
}

public BarData generateBar(){
        db=new SQLiteDatabaseHandler(this);
        BarData bar=new BarData();
        bar.setBarWidth(0.5f);
        ArrayList<BarEntry>moodValues=new ArrayList<>();
        moodValues=db.getSleepData(user);
        BarDataSet set2=new BarDataSet(moodValues,"Mood");
        set2.setStackLabels(new String[]{"Mood"});
        set2.setColor(R.color.teal_200);
        set2.setValueTextColor(Color.rgb(61, 165, 255));
        set2.setValueTextSize(10f);
        set2.setAxisDependency(YAxis.AxisDependency.LEFT);
        bar.addDataSet(set2);
        return bar;
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

public void combineGraph()
{
    CombinedChart Chart=findViewById(R.id.linechart);
    CombinedData combine=new CombinedData();
    combine.setData(GenerateLine());
    combine.setData(generateBar());
    ValueFormatter vf= new ValueFormatter() {
        @Override
        public String getFormattedValue(float value) {
            return""+(int)value;
        }
    };
    combine.setValueFormatter(vf);
    Chart.setData(combine);
    Chart.getAxisRight().setEnabled(false);
    Chart.setDrawGridBackground(false);
    Chart.getAxisRight().setDrawGridLines(false);
    Chart.getAxisLeft().setDrawGridLines(false);
    Chart.getXAxis().setDrawGridLines(false);
    Chart.setDragEnabled(true);
    Chart.setScaleEnabled(true);
    Chart.getXAxis().setAxisMaximum(generateBar().getXMax()+0.5f);
    Chart.getXAxis().setAxisMinimum(generateBar().getXMin()-0.5f);
    Chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

}



}