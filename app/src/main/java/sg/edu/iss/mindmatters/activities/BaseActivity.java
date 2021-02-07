package sg.edu.iss.mindmatters.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import sg.edu.iss.mindmatters.R;

public class BaseActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void callCustomActionBar(Context ctx, boolean settingsVisibility){
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);

        if(!getSharedPreferences("user_credentials", MODE_PRIVATE).contains("token")
            || !settingsVisibility){
            getSupportActionBar().setCustomView(R.layout.custom_action_bar_layout);

        }
        else{
            getSupportActionBar().setCustomView(R.layout.custom_action_bar_layout_left);

            ImageView settings  = (ImageView) findViewById(R.id.settingsBtn);
            settings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ctx, SettingsActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_to_left_enter,R.anim.right_to_left_exit);
                }
            });

        }

    }

}