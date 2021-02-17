package sg.edu.iss.mindmatters.activities.fragments.landing;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.CursorIndexOutOfBoundsException;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
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

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;
import sg.edu.iss.mindmatters.MyApplication;
import sg.edu.iss.mindmatters.R;
import sg.edu.iss.mindmatters.RetrofitClient;
import sg.edu.iss.mindmatters.activities.Alarms;
import sg.edu.iss.mindmatters.activities.DailyQuizActivity;
import sg.edu.iss.mindmatters.activities.GetHelpList;
import sg.edu.iss.mindmatters.activities.MainActivity;
import sg.edu.iss.mindmatters.activities.Resources;
import sg.edu.iss.mindmatters.dao.SQLiteDatabaseHandler;
import sg.edu.iss.mindmatters.model.QuizOutcome;
import sg.edu.iss.mindmatters.webview.QuizActivity;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class LandingFragment extends Fragment implements View.OnClickListener{

    View mView;

    public SharedPreferences pref;
    SQLiteDatabaseHandler db;
    String token;
    String user;

    int DAILY_DONE = 1;
    boolean RESPONSE_CODE = true;

    protected BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("date_update")) {
//                if(pref.contains("token"))
//                    loadNextDate();
            }
        }
    };

    public LandingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        if(db.countDb(pref.getString("username","user"))>0){
            pref = this.getActivity().getSharedPreferences(
                    "user_credentials", MODE_PRIVATE);
            token=pref.getString("token",null);
            populateDash(pref.getString("username","user"));
            combineGraph();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_landing, container, false);
        this.mView = view;
        pref = getActivity().getSharedPreferences(
                "user_credentials", MODE_PRIVATE);

        IntentFilter filter = new IntentFilter();
        filter.addAction("date_update");
        getActivity().registerReceiver(receiver, filter);

        token=pref.getString("token",null);
        user=pref.getString("username","user");

        TextView tv = (TextView) mView.findViewById(R.id.userNametv);
        tv.setText("Welcome, "+ user);
        if(!user.equals("user")){

            LinearLayout dashView=mView.findViewById(R.id.dash_support);
            dashView.setVisibility(View.VISIBLE);
            if(db.countDb(user)>0)
            {
            populateDash(user);
            combineGraph();
            }
            createNotificationChannel();
            runDailyQuiz();
            launchAlarm();
            mView.findViewById(R.id.floatingActionButton).setOnClickListener(this);
            mView.findViewById(R.id.floatingActionButton).setVisibility(View.VISIBLE);}


        MyApplication.setCurrentActivity("MainPage");

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.test_button){
            Intent intent = new Intent(getActivity(), QuizActivity.class);
            startActivity(intent);
        }
        else if(view.getId() == R.id.gethelp_button){
            Intent intent = new Intent(getActivity(), GetHelpList.class);
            startActivity(intent);
        }
        else if(view.getId() == R.id.resource_btn) {
            Intent intent = new Intent(getActivity(), Resources.class);
            startActivity(intent);
        }
        else if(view.getId() == R.id.floatingActionButton) {
            Intent intent = new Intent(getActivity(), DailyQuizActivity.class);
            startActivityForResult(intent, DAILY_DONE);
        }
    }

    public void runDailyQuiz(){
        try {
                if (db.findDailyByDate(LocalDate.now(), pref.getString("username","user")) == null) {
                    mView.findViewById(R.id.floatingActionButton).setVisibility(View.VISIBLE);
                }
                else{
                    mView.findViewById(R.id.floatingActionButton).setVisibility(View.GONE);
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
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
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

        NotificationManager notifMgr = getActivity().getSystemService(NotificationManager.class);
        notifMgr.createNotificationChannel(channel);
    }

    public void loadNextDate(){
        try {
            LocalDate getNextDate = getOutcome(pref.getString("token", null)).getNextQuiz();

            TextView nextDate = mView.findViewById(R.id.next_date_taken);
            nextDate.setVisibility(View.VISIBLE);
            TextView header = mView.findViewById(R.id.next_quiz_header);
            header.setText(R.string.next_quiz_header);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            nextDate.setText(getNextDate.format(dtf));
        }
        catch(NullPointerException e){
            TextView nextDate = mView.findViewById(R.id.next_date_taken);
            nextDate.setVisibility(View.VISIBLE);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            nextDate.setText(LocalDate.now().format(dtf));
            e.printStackTrace();
        }
    }

    public void launchAlarm(){
        try{
            if(getActivity().getSharedPreferences("Settings", MODE_PRIVATE).getString("alarm", "not set").contains("not set")){
                Alarms.startAction(getActivity());
            };
        }
        catch(NullPointerException e){
            Alarms.startAction(getActivity());
            e.printStackTrace();
        }
    }

    public QuizOutcome getOutcome(String token){

        Call<QuizOutcome> call = RetrofitClient
                .getInstance()
                .getAPI()
                .getUserProfile(token);
        try {
            Response<QuizOutcome> qo=call.execute();
            QuizOutcome oc= qo.body();
            return oc;
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }



   /* public void populateGraph()
    {
        LineChart linechart = (LineChart) mView.findViewById(R.id.linechart);
        linechart.setDragEnabled(true);
        linechart.setScaleEnabled(false);
        ArrayList<Entry> yValues=new ArrayList<>();
        ArrayList<Entry>xValues=new ArrayList<>();
        yValues=db.getMoodData(user);
        xValues=db.getSleepData(user);
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
        TextView averagesleep=mView.findViewById(R.id.averagesleep);
        TextView averagemood=mView.findViewById(R.id.averagemood);
        db=new SQLiteDatabaseHandler(getActivity());
        float avgMood=db.averageMoodData(user);
        averagemood.setText(String.format("%.1f",avgMood/10));
        float avgSlp=db.averageSleepData(user);
        averagesleep.setText(String.format("%.1f",avgSlp));
    new Thread(new Runnable() {
        @Override
        public void run() {
            String outcome=getOutcome(user).getQuizOutcome();
                    TextView text = mView.findViewById(R.id.currentStatus);
                    text.setText(outcome);
                    loadNextDate();
            }
        }).start();
    }*/

    public LineData GenerateLine()
    {
        db=new SQLiteDatabaseHandler(getActivity());
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
        db=new SQLiteDatabaseHandler(getActivity());
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
        TextView averagesleep=mView.findViewById(R.id.averagesleep);
        TextView averagemood=mView.findViewById(R.id.averagemood);
        db=new SQLiteDatabaseHandler(getActivity());
        float avgMood=db.averageMoodData(user);
        averagemood.setText(String.format("%.1f",avgMood/10));
        float avgSlp=db.averageSleepData(user);
        averagesleep.setText(String.format("%.1f",avgSlp));
        new Thread(new Runnable() {
            @Override
            public void run() {
                String outcome="";
                if(getOutcome(pref.getString("token",null))!=null){
                    outcome=getOutcome(pref.getString("token",null)).getQuizOutcome();
                }
                TextView text = mView.findViewById(R.id.currentStatus);
                text.setText(outcome);

            }
        }).start();
    }

    public void combineGraph()
    {
        CombinedChart Chart=mView.findViewById(R.id.linechart);
        CombinedData combine=new CombinedData();
        combine.setData(GenerateLine());
        combine.setData(generateBar());
        final ArrayList<String> xAxisLabel = new ArrayList<>();
            xAxisLabel.add("Day 1");
            xAxisLabel.add("Tue");
            xAxisLabel.add("Wed");
            xAxisLabel.add("Thu");
            xAxisLabel.add("Fri");
            xAxisLabel.add("Sat");
            xAxisLabel.add("Sun");
        ValueFormatter vf= new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return xAxisLabel.get((int) value);
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
        Chart.getDescription().setEnabled(false);

    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        db = new SQLiteDatabaseHandler(getActivity());
    }

}