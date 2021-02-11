package sg.edu.iss.mindmatters.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import sg.edu.iss.mindmatters.R;

public class GeneralSettings extends BaseActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_settings);

        callCustomActionBar(this, false);

        Switch aSwitch = findViewById(R.id.switch1);
        checkAlarmStatus(aSwitch);
        aSwitch.setOnCheckedChangeListener(this);
        findViewById(R.id.done).setOnClickListener(this);
    }

    public void checkAlarmStatus(Switch aSwitch){
        if(getSharedPreferences("Settings", MODE_PRIVATE).getString("alarm", "off").contains("on")){
            aSwitch.setChecked(true);
        }
        else{
            aSwitch.setChecked(false);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked) {
            Alarms.startAction(this);
            Toast.makeText(this, "Daily Quiz alarm set", Toast.LENGTH_SHORT).show();
        }
        else{
            Alarms.stopAction(this);
            Toast.makeText(this, "Daily Quiz alarm off", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.done) {
            finish();
        }
    }
}