package sg.edu.iss.mindmatters.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Toast;

import java.util.Calendar;

import sg.edu.iss.mindmatters.AlarmReceiver;
import sg.edu.iss.mindmatters.dao.Notification_receiver;

public class Alarms {

    public static void startAction(Context context){
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction("alarm");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 02);
        calendar.set(Calendar.MINUTE, 57);

        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1 * 60 * 1000, pendingIntent);

        SharedPreferences sharedPref = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("alarm", "on");
        editor.commit();

        Toast.makeText(context, "Daily Quiz alarm set", Toast.LENGTH_SHORT).show();

    }

    public static void stopAction(Context context){
        Intent intent = new Intent(context, Notification_receiver.class);
        intent.setAction("alarm");

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pendingIntent);

        SharedPreferences sharedPref = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("alarm", "off");
        editor.commit();

        Toast.makeText(context, "Daily Quiz alarm off", Toast.LENGTH_SHORT).show();
    }

    public static void dailyTips(Context context){
        Calendar calender = Calendar.getInstance();
        calender.setTimeInMillis(System.currentTimeMillis());
        calender.set(Calendar.HOUR_OF_DAY, 12);
        calender.set(Calendar.MINUTE, 36);

        Intent intent = new Intent(context, Notification_receiver.class);
        intent.setAction("notif");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,100,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarm =
                (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, calender.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

        SharedPreferences sharedPref = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("value", "on");
        editor.commit();

        Toast.makeText(context, "Daily tips notification on", Toast.LENGTH_SHORT).show();
    }

    public static void stopDailyTips(Context context){
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction("notif");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,100,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarm =
                (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pendingIntent);

        SharedPreferences sharedPref = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("value", "off");
        editor.commit();

        Toast.makeText(context, "Daily tips notification off", Toast.LENGTH_SHORT).show();
    }

}
