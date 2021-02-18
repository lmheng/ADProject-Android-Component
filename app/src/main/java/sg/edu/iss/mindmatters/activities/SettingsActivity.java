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
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONObject;

import java.io.PipedInputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sg.edu.iss.mindmatters.R;
import sg.edu.iss.mindmatters.RetrofitClient;
import sg.edu.iss.mindmatters.model.User;
import androidx.appcompat.widget.SwitchCompat;

import java.util.Calendar;

import sg.edu.iss.mindmatters.R;
import sg.edu.iss.mindmatters.dao.Notification_receiver;

public class SettingsActivity extends BaseActivity implements View.OnClickListener {
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        callCustomActionBar(SettingsActivity.this,false);
        SharedPreferences pref = getSharedPreferences(
                "user_credentials", MODE_PRIVATE);
        if(!pref.getString("loginMethod","none").equals("email")){
            findViewById(R.id.editProfSet).setVisibility(View.GONE);
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        if(pref.contains("token")){
            findViewById(R.id.editProfSet).setOnClickListener(this);
            findViewById(R.id.logoutSet).setOnClickListener(this);
            findViewById(R.id.generalSettings).setOnClickListener(this);
            findViewById(R.id.deleteProfSet).setOnClickListener(this);
            findViewById(R.id.loginset).setVisibility(View.GONE);
        }
        else{
            findViewById(R.id.loginset).setOnClickListener(this);
            findViewById(R.id.generalSettings).setOnClickListener(this);
            findViewById(R.id.editProfSet).setVisibility(View.GONE);
            findViewById(R.id.logoutSet).setVisibility(View.GONE);
            findViewById(R.id.deleteProfSet).setVisibility(View.GONE);
        }

    }

    public void logout(){
        disconnectFromFacebook();
        disconnectFromGoogle();
        clearDetails();
        Intent intent =new Intent(SettingsActivity.this, LoginActivity.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void disconnectFromFacebook(){
        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }
        LoginManager.getInstance().logOut();
    }

    public void disconnectFromGoogle(){
        mGoogleSignInClient.signOut();
    }

    public void editProfile(){
        Call<User> call = RetrofitClient
                .getInstance()
                .getAPI()
                .getInfo(getSharedPreferences("user_credentials", MODE_PRIVATE)
                        .getString("token",null));
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 200) {
                  User responseUser = response.body();
                  if(responseUser!=null){
                      Intent intent =new Intent(SettingsActivity.this, EditUserActivity.class);
                      intent.putExtra("email",responseUser.getEmail());
                      intent.putExtra("phone",responseUser.getPhone());
                      intent.putExtra("userName",responseUser.getUserName());
                      startActivity(intent);
                  }
                } else {
                    Toast.makeText(SettingsActivity.this, "Something went wrong!Try again later", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(SettingsActivity.this, "Something went wrong!Try again later", Toast.LENGTH_LONG).show();
            }
        });

    }

    public void generalSettings(){
        Intent intent =new Intent(SettingsActivity.this, GeneralSettings.class);
        startActivity(intent);
    }

    public void login(){
        Intent intent =new Intent(SettingsActivity.this, LoginActivity.class);
        startActivity(intent);
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

        if(v.getId()==R.id.deleteProfSet) {
            deleteProfile();
        }

        if(v.getId()==R.id.loginset){
            login();
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


    public void deleteProfile(){
        Intent intent =new Intent(SettingsActivity.this, DeleteActivity.class);
        startActivity(intent);
    }

}