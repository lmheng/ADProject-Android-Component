package sg.edu.iss.mindmatters.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import sg.edu.iss.mindmatters.R;

public class UserMessageActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_message);
        callCustomActionBar(UserMessageActivity.this, false);
        loadComponents();
    }

    private void loadComponents() {
        TextView messageTitle = (TextView) findViewById(R.id.userMsgSubtitle);
        TextView message = (TextView) findViewById(R.id.userMsgText);
        if (getIntent().getStringExtra("msg").equals("registered")) {
            messageTitle.setText("Account Created!");
            message.setText("Please check your email to verify your account." +
                    "\n\nIf you have not received any email, email to team1.sa51@gmail.com for support ");
        } else if (getIntent().getStringExtra("msg").equals("resetPwd")) {
            messageTitle.setText("Reset password link sent!");
            message.setText("Please check your email to reset your password." +
                    "\n\nIf you have not received any email, email to team1.sa51@gmail.com for support ");
        } else if (getIntent().getStringExtra("msg").equals("edited")) {
            messageTitle.setText("Saved Successfully!");
            if (getIntent().getBooleanExtra("passEdit", false)) {
                String displayMessage = getIntent().getStringExtra("content") + "\n"
                        + "Please login with your new password";
                message.setText(displayMessage);
            } else {
                message.setText(getIntent().getStringExtra("content"));
            }
        }
        else if(getIntent().getStringExtra("msg").equals("deleted")){
            messageTitle.setText("Account Deleted Successfully!");
            message.setText("Thank you for using our services");
        }
        findViewById(R.id.btnVerifyOk).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnVerifyOk) {
            Intent intent;
            if (getIntent().getStringExtra("msg").equals("edited")) {
                if (getIntent().getBooleanExtra("passEdit", false)
                ) {
                    SharedPreferences pref = getSharedPreferences(
                            "user_credentials", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.clear();
                    editor.commit();
                } else {
                    finish();
                }
            }
            if (getIntent().getStringExtra("msg").equals("deleted")) {
                    SharedPreferences pref = getSharedPreferences(
                            "user_credentials", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.clear();
                    editor.commit();

            }

            intent = new Intent(UserMessageActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}