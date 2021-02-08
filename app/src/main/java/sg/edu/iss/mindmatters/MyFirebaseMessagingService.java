package sg.edu.iss.mindmatters;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {


    public MyFirebaseMessagingService() {
        super();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(Application.NOTIFICATION_SERVICE, "myFirebaseMessagingService - onMessageReceived - message: " + remoteMessage);

        Intent dialogIntent = new Intent(this, NotificationActivity.class);
        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        dialogIntent.putExtra("msg", remoteMessage);
        startActivity(dialogIntent);

    }

}