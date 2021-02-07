package sg.edu.iss.mindmatters.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

import sg.edu.iss.mindmatters.R;
import sg.edu.iss.mindmatters.dao.SQLiteDatabaseHandler;
import sg.edu.iss.mindmatters.model.DailyQuiz;

public class DailyQuizActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private SQLiteDatabaseHandler db;

    SeekBar simpleSeekBar;
    int progressChangedValue = 0;
    String sleepOutcome = "";
    int sleepHours = 0;

    String[] sleep = { "Excellent", "Very Good", "Average", "Poor" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //change once HueyLi is done
        setContentView(R.layout.daily_quiz_popup);
        callCustomActionBar(DailyQuizActivity.this,false);

        loadView();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btnSubmit) {
            createNewQuizEntry();
            Toast.makeText(view.getContext(), "Entry submitted!", Toast.LENGTH_SHORT).show();
            try {
                List<DailyQuiz> dailies = db.allQuiz();

                if(dailies != null) {
                    for(DailyQuiz daily : dailies) {
                        System.out.println(daily.toString());
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            finish();
        }
    }

    public void loadView(){
        simpleSeekBar = findViewById(R.id.seek_bar);
        simpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                progressChangedValue = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //Toast.makeText(DailyQuizActivity.this, "Selected:" + progressChangedValue, Toast.LENGTH_SHORT).show();
            }
        });

        Spinner spin = (Spinner) findViewById(R.id.spinner1);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sleep);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
        spin.setOnItemSelectedListener(this);

        NumberPicker picker1 = findViewById(R.id.picker1);
        picker1.setMaxValue(24);
        picker1.setMinValue(0);

        picker1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                //Toast.makeText(DailyQuizActivity.this, "Selected:" + i1, Toast.LENGTH_SHORT).show();
                sleepHours = i1;
            }
        });

        Button button = findViewById(R.id.btnSubmit);
        button.setOnClickListener(this);

    }

    public void createNewQuizEntry(){
        db = new SQLiteDatabaseHandler(this);

        int q1 = progressChangedValue;
        String q2 = sleepOutcome;
        int q3 = sleepHours;

        SharedPreferences pref = getSharedPreferences(
                "user_credentials", MODE_PRIVATE);

        System.out.println(pref.getString("username","user"));

        DailyQuiz quiz = new DailyQuiz();
        quiz.setQ1(q1);
        quiz.setQ2(q2);
        quiz.setQ3(q3);

        Calendar today = Calendar.getInstance();
        quiz.setDate(today);
        quiz.setUsername(pref.getString("username","user"));

        db.createQuizEntry(quiz);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        //Toast.makeText(getApplicationContext(), "Selected: "+ sleep[position] ,Toast.LENGTH_SHORT).show();
        sleepOutcome = sleep[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}