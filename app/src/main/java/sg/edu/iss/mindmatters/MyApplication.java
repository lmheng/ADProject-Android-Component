package sg.edu.iss.mindmatters;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import sg.edu.iss.mindmatters.dao.Notification_receiver;

public class MyApplication extends Application implements Application.ActivityLifecycleCallbacks, ComponentCallbacks2 {

    NetworkReceiver networkReceiver = new NetworkReceiver();
    AlarmReceiver alarmReceiver = new AlarmReceiver();
    Notification_receiver notifReceiver = new Notification_receiver();
    private static String mCurrentActivity = null;

    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(this);

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkReceiver, filter);

        IntentFilter filter1 = new IntentFilter("alarm");
        registerReceiver(alarmReceiver, filter1);

        IntentFilter filter2 = new IntentFilter("notif");
        registerReceiver(notifReceiver, filter2);
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {

    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {

    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkReceiver, filter);
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {

    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        this.unregisterReceiver(networkReceiver);
    }

    public static String getCurrentActivity(){
        return mCurrentActivity;
    }
    public static void setCurrentActivity(String currentActivity){
        mCurrentActivity = currentActivity;
    }

}
