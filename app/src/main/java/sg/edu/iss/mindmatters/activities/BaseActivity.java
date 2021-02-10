package sg.edu.iss.mindmatters.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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