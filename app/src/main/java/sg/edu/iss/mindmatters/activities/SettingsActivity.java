package sg.edu.iss.mindmatters.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import sg.edu.iss.mindmatters.R;

public class SettingsActivity extends BaseActivity implements View.OnClickListener {

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

            findViewById(R.id.loginset).setVisibility(View.GONE);
        }
        else{
            findViewById(R.id.loginset).setOnClickListener(this);

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
}