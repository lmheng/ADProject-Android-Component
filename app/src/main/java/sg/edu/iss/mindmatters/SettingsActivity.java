package sg.edu.iss.mindmatters;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SettingsActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        callCustomActionBar(SettingsActivity.this,false);
        findViewById(R.id.editProfSet).setOnClickListener(this);
        findViewById(R.id.logoutSet).setOnClickListener(this);

    }

    public void logout(){
        SharedPreferences pref = getSharedPreferences(
                "user_credentials", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
        Intent intent =new Intent(SettingsActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void editProfile(){
        Intent intent =new Intent(SettingsActivity.this, EditUserActivity.class);
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
    }
}