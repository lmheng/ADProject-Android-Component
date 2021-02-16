package sg.edu.iss.mindmatters.activities;

import android.content.Intent;
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
import java.time.LocalDate;
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
        setContentView(R.layout.daily_quiz_popup);
        callCustomActionBar(DailyQuizActivity.this,true);

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
            Intent response = new Intent();
            response.putExtra("action", true);
            setResult(RESULT_OK, response);
            finish();
        }
        else if(view.getId() == R.id.btnDoLater) {
            Intent response = new Intent();
            response.putExtra("action", false);
            setResult(RESULT_CANCELED, response);
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

        picker1.setOnValueChangedListener((numberPicker, i, i1) -> {
            sleepHours = i1;
        });

        Button button = findViewById(R.id.btnSubmit);
        button.setOnClickListener(this);

        Button btn = findViewById(R.id.btnDoLater);
        btn.setOnClickListener(this);

    }

    public void createNewQuizEntry(){
        db = new SQLiteDatabaseHandler(this);

        int q1 = progressChangedValue;
        String q2 = sleepOutcome;
        int q3 = sleepHours;

        SharedPreferences pref = getSharedPreferences(
                "user_credentials", MODE_PRIVATE);

        DailyQuiz quiz = new DailyQuiz();
        quiz.setQ1(q1);
        quiz.setQ2(q2);
        quiz.setQ3(q3);
        quiz.setDate(LocalDate.now());
        quiz.setUsername(pref.getString("username","user"));

        db.createQuizEntry(quiz);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        sleepOutcome = sleep[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


}