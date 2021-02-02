package sg.edu.iss.mindmatters;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class GetHelp extends AppCompatActivity implements View.OnClickListener{

    public static final String EXTERNAL_URL = "externalUrl";
    public final int buttonArr[] = {R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5, R.id.button6, R.id.button7, R.id.button8, R.id.button9, R.id.button10};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_help);
        callCustomActionBar();

        for(int id : buttonArr) {
            findViewById(id).setVisibility(View.VISIBLE);
            findViewById(id).setOnClickListener(this);
        }

//        View btn1 = findViewById(R.id.button1);
//        btn1.setOnClickListener(this);
//        View btn3 = findViewById(R.id.button3);
//        btn3.setOnClickListener(this);
//        View btn5 = findViewById(R.id.button5);
//        btn5.setOnClickListener(this);
//        View btn7 = findViewById(R.id.button7);
//        btn7.setOnClickListener(this);
//        View btn9 = findViewById(R.id.button9);
//        btn9.setOnClickListener(this);
//
//        View btn2 = findViewById(R.id.button2);
//        btn2.setOnClickListener(this);
//        View btn4 = findViewById(R.id.button4);
//        btn4.setOnClickListener(this);
//        View btn6 = findViewById(R.id.button6);
//        btn6.setOnClickListener(this);
//        View btn8 = findViewById(R.id.button8);
//        btn8.setOnClickListener(this);
//        View btn10 = findViewById(R.id.button10);
//        btn10.setOnClickListener(this);
    }

    @Override
    protected void onResume(){
        super.onResume();
        for(int id : buttonArr) {
            findViewById(id).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){

            case R.id.button2:
                Intent intent2 = new Intent(Intent.ACTION_DIAL);
                intent2.setData(Uri.parse("tel:18002026868"));
                startActivity(intent2);
                break;

            case R.id.button4:
                Intent intent4 = new Intent(Intent.ACTION_DIAL);
                intent4.setData(Uri.parse("tel:63892222"));
                startActivity(intent4);
                break;

            case R.id.button6:
                Intent intent6 = new Intent(Intent.ACTION_DIAL);
                intent6.setData(Uri.parse("tel:18002214444"));
                startActivity(intent6);
                break;

            case R.id.button8:
                Intent intent8 = new Intent(Intent.ACTION_DIAL);
                intent8.setData(Uri.parse("tel:63853714"));
                startActivity(intent8);
                break;

            case R.id.button10:
                Intent intent10 = new Intent(Intent.ACTION_DIAL);
                intent10.setData(Uri.parse("tel:18003772252"));
                startActivity(intent10);
                break;

            case R.id.button1:
                String externalUrl1 =
                        "https://www.gov.sg/article/call-these-helplines-if-you-need-emotional-or-psychological-support";
                launchExternalPage(externalUrl1);
                break;

            case R.id.button3:
                String externalUrl3 =
                        "https://www.imh.com.sg/contact-us/";
                launchExternalPage(externalUrl3);
                break;

            case R.id.button5:
                String externalUrl5 =
                        "https://www.sos.org.sg/contact-us";
                launchExternalPage(externalUrl5);
                break;

            case R.id.button7:
                String externalUrl7 =
                        "https://www.silverribbonsingapore.com/counselling.html";
                launchExternalPage(externalUrl7);
                break;

            case R.id.button9:
                String externalUrl9 =
                        "https://www.touch.org.sg/about-touch/our-services/touch-cyber-wellness-homepage/contact-us";
                launchExternalPage(externalUrl9);
                break;
        }

    }

    void launchExternalPage(String externalUrl) {
        Intent intent = new Intent(GetHelp.this, GetHelpView.class);
        intent.putExtra(EXTERNAL_URL, externalUrl);
        startActivity(intent);

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