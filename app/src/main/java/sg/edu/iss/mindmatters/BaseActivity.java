package sg.edu.iss.mindmatters;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class BaseActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void callCustomActionBar(Context ctx,boolean settingsVisibility){
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar_layout);
        if(!getSharedPreferences("user_credentials", MODE_PRIVATE).contains("token")
            || !settingsVisibility){
            findViewById(R.id.settingsBtn).setVisibility(View.GONE);
        }
        ImageView settings  = (ImageView)findViewById(R.id.settingsBtn);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ctx, SettingsActivity.class);
                startActivity(intent);
            }
        });
    }

}