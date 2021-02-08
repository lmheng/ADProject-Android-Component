package sg.edu.iss.mindmatters.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import sg.edu.iss.mindmatters.MyApplication;
import sg.edu.iss.mindmatters.webview.QuizActivity;
import sg.edu.iss.mindmatters.R;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    SharedPreferences pref;
    private LineChart linechart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        linechart=(LineChart)findViewById(R.id.linechart);
        linechart.setDragEnabled(true);
        linechart.setScaleEnabled(false);
        ArrayList<Entry>yValues=new ArrayList<>();
        ArrayList<Entry>xValues=new ArrayList<>();
        yValues.add(new Entry(1,50f));
        yValues.add(new Entry(2,80f));
        yValues.add(new Entry(3,50f));
        yValues.add(new Entry(4,30f));
        yValues.add(new Entry(5,20f));
        yValues.add(new Entry(6,107f));
        yValues.add(new Entry(7,80f));
        xValues.add(new Entry(1,30f));
        xValues.add(new Entry(2,25f));
        xValues.add(new Entry(3,40f));
        xValues.add(new Entry(4,33f));
        xValues.add(new Entry(5,99f));
        xValues.add(new Entry(6,10f));
        xValues.add(new Entry(7,57f));

        LineDataSet set2=new LineDataSet(xValues,"dataSet2");
        LineDataSet set1=new LineDataSet(yValues,"dataSet1");
        set1.setFillAlpha(110);
        set1.setFillColor(android.R.color.black);
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

        pref = getSharedPreferences(
                "user_credentials", MODE_PRIVATE);
        TextView tv = (TextView)findViewById(R.id.userNametv);
        tv.setText("Welcome, "+pref.getString("username","user"));

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



}