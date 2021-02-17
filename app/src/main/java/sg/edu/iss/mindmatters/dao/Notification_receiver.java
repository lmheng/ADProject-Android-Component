package sg.edu.iss.mindmatters.dao;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sg.edu.iss.mindmatters.API;
import sg.edu.iss.mindmatters.R;
import sg.edu.iss.mindmatters.activities.Alarms;
import sg.edu.iss.mindmatters.activities.LoginActivity;
import sg.edu.iss.mindmatters.model.DailyTips;

public class Notification_receiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "888888";

    @Override
    public void onReceive(Context context, Intent intent) {
        populateNotification(context,intent);
    }

    public void populateNotification(Context context, Intent intent) {

        System.out.println("Notification set");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        API mindMattersApi = retrofit.create(API.class);

        Call<List<DailyTips>> call = mindMattersApi.getDailyTips();

        call.enqueue(new Callback<List<DailyTips>>() {
            @Override
            public void onResponse(Call<List<DailyTips>> call, Response<List<DailyTips>> response) {

                if (!response.isSuccessful()) {
                    return;
                }

                List<DailyTips> dailyTips = response.body();
                String quote = "";

                for (DailyTips dailyTip : dailyTips) {
                    quote += "" + dailyTip.getQuotes();
                }
                createNotification(context,intent,quote);
            }

            @Override
            public void onFailure(Call<List<DailyTips>> call, Throwable t) {
                t.getMessage();
            }
        });

    }

    public void createNotification(Context context, Intent intent,String content)
    {
        Intent repeating_intent = new Intent(context, LoginActivity.class);
        repeating_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 100, repeating_intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        builder
                .setSmallIcon(R.drawable.mindmatters_logo)
                .setContentTitle("DailyTips")
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        int notificationId = 11111;
        NotificationManagerCompat mgr =
                NotificationManagerCompat.from(context);
        Notification notification = builder.build();
        mgr.notify(notificationId, notification);
    }

}
