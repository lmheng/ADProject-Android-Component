package sg.edu.iss.mindmatters.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import sg.edu.iss.mindmatters.R;

public class GeneralSettings extends BaseActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    Switch mySwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_settings);
        callCustomActionBar(this, false);

        SharedPreferences pref = getSharedPreferences("user_credentials", MODE_PRIVATE);
        switchCase();
        findViewById(R.id.done).setOnClickListener(this);

        if(pref.contains("token"))
        {
            Switch aSwitch = findViewById(R.id.switch1);
            checkAlarmStatus(aSwitch);
            aSwitch.setOnCheckedChangeListener(this);
        }
        else{
            checkAlarmStatus(mySwitch);
            findViewById(R.id.dailyQuizAlarm).setVisibility(View.GONE);
        }

    }

    public void checkAlarmStatus(Switch aSwitch){
        if(getSharedPreferences("Settings", MODE_PRIVATE).getString("alarm", "off").contains("on")){
            aSwitch.setChecked(true);
        }
        else{
            aSwitch.setChecked(false);
        }

        if(getSharedPreferences("Settings", MODE_PRIVATE).getString("value", "off").contains("on")){
            mySwitch.setChecked(true);
        }
        else{
            mySwitch.setChecked(false);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(buttonView == mySwitch)
        {
            if (isChecked) {
                Alarms.dailyTips(this);
            } else {
                Alarms.stopDailyTips(this);
            }
        }
        else {
            if (isChecked) {
                Alarms.startAction(this);
            } else {
                Alarms.stopAction(this);
            }
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.done) {
            finish();
        }
    }

    public void switchCase(){
        mySwitch = findViewById(R.id.notifyBtn);
        mySwitch.setOnCheckedChangeListener(this);
    }


}