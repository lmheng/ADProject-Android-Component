package sg.edu.iss.mindmatters;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class NoNetwork extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_network);
        callCustomActionBar();

        MyApplication.setCurrentActivity("NoNetwork");

        Button button = findViewById(R.id.retry_button);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

            if(view.getId() == R.id.retry_button) {
                if(NetworkReceiver.checkInternet(this))
                {
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(this, "Please check if you are connected to the internet", Toast.LENGTH_LONG).show();
                }
            }
    }

    public void callCustomActionBar(){
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar_layout);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == R.id.settings) {
            Intent intent = new Intent();
            startActivity(intent);
        }
        return true;
    }
}