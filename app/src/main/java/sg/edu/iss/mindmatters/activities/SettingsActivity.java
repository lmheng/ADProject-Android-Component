package sg.edu.iss.mindmatters.activities;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.SwitchCompat;

import java.util.Calendar;

import sg.edu.iss.mindmatters.R;
import sg.edu.iss.mindmatters.dao.Notification_receiver;

public class SettingsActivity extends BaseActivity implements View.OnClickListener {
    SwitchCompat mySwitch;
    private static final String CHANNEL_ID = "888888";
    private static final String CHANNEL_NAME = "Message Notification Channel";
    private static final String CHANNEL_DESCRIPTION = "This channel is for displaying messages";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        callCustomActionBar(SettingsActivity.this,false);

        SharedPreferences pref = getSharedPreferences("user_credentials", MODE_PRIVATE);

        if(pref.contains("token")){
            findViewById(R.id.editProfSet).setOnClickListener(this);
            findViewById(R.id.logoutSet).setOnClickListener(this);
            findViewById(R.id.generalSettings).setOnClickListener(this);
            findViewById(R.id.notifyBtn).setOnClickListener(this);

            findViewById(R.id.loginset).setVisibility(View.GONE);
        }
        else{
            findViewById(R.id.loginset).setOnClickListener(this);
            findViewById(R.id.notifyBtn).setOnClickListener(this);

            findViewById(R.id.editProfSet).setVisibility(View.GONE);
            findViewById(R.id.logoutSet).setVisibility(View.GONE);
            findViewById(R.id.generalSettings).setVisibility(View.GONE);
        }

    }

    public void logout(){
        clearDetails();
        Intent intent =new Intent(SettingsActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void editProfile(){
        Intent intent =new Intent(SettingsActivity.this, EditUserActivity.class);
        startActivity(intent);
    }

    public void generalSettings(){
        Intent intent =new Intent(SettingsActivity.this, GeneralSettings.class);
        startActivity(intent);
    }

    public void login(){
        Intent intent =new Intent(SettingsActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public void switchCase(){
        createNotificationChannel();
        mySwitch = findViewById(R.id.notifyBtn);
        SharedPreferences sharedPreferences = getSharedPreferences("save", MODE_PRIVATE);
        mySwitch.setChecked(sharedPreferences.getBoolean("value", true));
        if(mySwitch.isChecked()==true){
            dailyTips();
        }else{
          stopDailyTips();
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.editProfSet){
            editProfile();
        }
        if(v.getId()==R.id.logoutSet){
            logout();
        }
        if(v.getId()==R.id.generalSettings){
            generalSettings();
        }
        if(v.getId()==R.id.loginset){
            login();
        }
        if(v.getId()==R.id.notifyBtn){
            switchCase();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_to_right_enter, R.anim.left_to_right_exit);
    }
    private void clearDetails(){
        SharedPreferences pref = getSharedPreferences(
                "user_credentials", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }

    public void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
            channel.setDescription(CHANNEL_DESCRIPTION);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void dailyTips(){
        Calendar calender = Calendar.getInstance();

        calender.set(Calendar.HOUR_OF_DAY,21);
        calender.set(Calendar.MINUTE,38);
        calender.set(Calendar.SECOND,00);

        Intent intent = new Intent(getApplicationContext(), Notification_receiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),100,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarm =
                (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, calender.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
    }

    public void stopDailyTips(){
        Intent intent = new Intent(getApplicationContext(), Notification_receiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),100,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarm =
                (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pendingIntent);
    }




}