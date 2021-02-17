package sg.edu.iss.mindmatters;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.CursorIndexOutOfBoundsException;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.text.ParseException;
import java.time.LocalDate;

import sg.edu.iss.mindmatters.activities.Alarms;
import sg.edu.iss.mindmatters.activities.LoginActivity;
import sg.edu.iss.mindmatters.dao.SQLiteDatabaseHandler;

import static android.content.Context.MODE_PRIVATE;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        Toast.makeText(context, "Daily Quiz receiver", Toast.LENGTH_LONG).show();

        SQLiteDatabaseHandler db = new SQLiteDatabaseHandler(context);
        SharedPreferences pref = context.getSharedPreferences(
                "user_credentials", MODE_PRIVATE);

        if (action == null)
            return;
        try {
            if (action.equals("alarm") && db.findDailyByDate(LocalDate.now(), pref.getString("username","user")) == null) {
                String text = "Hi " + pref.getString("username", "user") +"! Remember to do your daily check-in with us!";
                createNotification(context, text);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        catch(CursorIndexOutOfBoundsException e) {
            String text = "Hi " + pref.getString("username", "user") +"! Remember to do your daily check-in with us!";
            createNotification(context, text);
            e.printStackTrace();
        }

    }

    private void createNotification(Context context, String text){

        Intent intent =
                new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0, intent, 0);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "888888");
        builder.setSmallIcon(R.drawable.mindmatters_logo).setContentTitle("Daily Quiz").setContentText(text).setPriority(NotificationCompat.PRIORITY_DEFAULT).setAutoCancel(true).setContentIntent(pendingIntent);
        Notification notification = builder.build();

        NotificationManagerCompat mgr = NotificationManagerCompat.from(context);
        mgr.notify(1, notification);
    }
}
