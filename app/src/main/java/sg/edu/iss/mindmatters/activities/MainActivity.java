package sg.edu.iss.mindmatters.activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.view.View;
import android.widget.TextView;

//import com.github.mikephil.charting.charts.LineChart;
//import com.github.mikephil.charting.data.Entry;
//import com.github.mikephil.charting.data.LineData;
//import com.github.mikephil.charting.data.LineDataSet;
//import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
//import com.github.mikephil.charting.listener.OnChartGestureListener;
//import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;

import sg.edu.iss.mindmatters.MyApplication;
import sg.edu.iss.mindmatters.dao.SQLiteDatabaseHandler;
import sg.edu.iss.mindmatters.webview.QuizActivity;
import sg.edu.iss.mindmatters.R;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    SharedPreferences pref;
    //private LineChart linechart;
    SQLiteDatabaseHandler db = new SQLiteDatabaseHandler(this);

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

//        linechart=(LineChart)findViewById(R.id.linechart);
//        linechart.setDragEnabled(true);
//        linechart.setScaleEnabled(false);
//        ArrayList<Entry>yValues=new ArrayList<>();
//        ArrayList<Entry>xValues=new ArrayList<>();
//        yValues.add(new Entry(1,50f));
//        yValues.add(new Entry(2,80f));
//        yValues.add(new Entry(3,50f));
//        yValues.add(new Entry(4,30f));
//        yValues.add(new Entry(5,20f));
//        yValues.add(new Entry(6,107f));
//        yValues.add(new Entry(7,80f));
//        xValues.add(new Entry(1,30f));
//        xValues.add(new Entry(2,25f));
//        xValues.add(new Entry(3,40f));
//        xValues.add(new Entry(4,33f));
//        xValues.add(new Entry(5,99f));
//        xValues.add(new Entry(6,10f));
//        xValues.add(new Entry(7,57f));
//
//        LineDataSet set2=new LineDataSet(xValues,"dataSet2");
//        LineDataSet set1=new LineDataSet(yValues,"dataSet1");
//        set1.setFillAlpha(110);
//        set1.setFillColor(android.R.color.black);
//        set1.setLineWidth(3f);
//        set1.setDrawFilled(true);
//        set2.setFillAlpha(85);
//        set2.setFillColor(android.R.color.black);
//        set2.setLineWidth(3f);
//        set2.setDrawFilled(true);
//        ArrayList<ILineDataSet>datasets=new ArrayList<>();
//        datasets.add(set1);
//        datasets.add(set2);
//
//        LineData data = new LineData(datasets);
//        linechart.setData(data);

        pref = getSharedPreferences(
                "user_credentials", MODE_PRIVATE);

        IntentFilter filter = new IntentFilter();
        filter.addAction("date_update");
        registerReceiver(receiver, filter);


        TextView tv = (TextView)findViewById(R.id.userNametv);
        tv.setText("Welcome, "+ pref.getString("username","user"));

        if(!pref.contains("token")){
            findViewById(R.id.next_date_taken).setVisibility(View.GONE);
            findViewById(R.id.floatingActionButton).setVisibility(View.GONE);
            TextView header = findViewById(R.id.next_quiz_header);
            header.setText("Please log in if you want to view full details");
        }
        else{
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
      if(pref.contains("token"))
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

}