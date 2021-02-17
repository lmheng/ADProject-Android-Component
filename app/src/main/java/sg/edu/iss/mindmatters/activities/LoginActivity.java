package sg.edu.iss.mindmatters.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sg.edu.iss.mindmatters.R;
import sg.edu.iss.mindmatters.RetrofitClient;
import sg.edu.iss.mindmatters.activities.fragments.LandingActivity;
import sg.edu.iss.mindmatters.activities.fragments.OnboardingActivity;
import sg.edu.iss.mindmatters.model.User;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText etUsername, etPassword;
    SharedPreferences pref;
    String userName, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        callCustomActionBar(LoginActivity.this, false);
        checkOnboarding();
        loadComponents();
    }

    private void loadComponents() {
        etUsername = findViewById(R.id.etUserName);
        etPassword = findViewById(R.id.etPassword);

        TextView registertv = findViewById(R.id.tvRegisterLink);
        String text = "<font>Don't have an account? Create one</font><font color=#0000FF><u> HERE!<u></font>";
        registertv.setText(Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY));

        pref = getSharedPreferences(
                "user_credentials", MODE_PRIVATE);

        if (pref.contains("token")) {
            Intent intent = new Intent(LoginActivity.this, LandingActivity.class);
            intent.putExtra("username", pref.getString("username", "User"));
            intent.putExtra("token", pref.getString("token", "Token"));
            startActivity(intent);
            finish();
        }

        findViewById(R.id.btnLogin).setOnClickListener(this);
        findViewById(R.id.tvWoLogin).setOnClickListener(this);
        findViewById(R.id.tvRegisterLink).setOnClickListener(this);
        findViewById(R.id.forgotLink).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnLogin) {
            userName = etUsername.getText().toString().trim();
            password = etPassword.getText().toString();
            loginUser(userName, password);
            finish();
        } else if (id == R.id.tvWoLogin) {
            Intent intent = new Intent(LoginActivity.this, LandingActivity.class);
            startActivity(intent);
        } else if (id == R.id.tvRegisterLink) {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        } else if (id == R.id.forgotLink) {
            startActivity(new Intent(LoginActivity.this, ForgotPwdActivity.class));
        }
    }

    private boolean validate(String userName, String password) {
        if (userName.isEmpty()) {
            etUsername.setError("Username is required");
            etUsername.requestFocus();
            return false;
        } else if (password.isEmpty()) {
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return false;
        }
        return true;
    }

    private void loginUser(String userName, String password) {
        if (!validate(userName, password)) {
            return;
        }
        User u = new User();
        u.setUserName(userName);
        u.setPassword(password);
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getAPI()
                .checkUser(u);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    String email = "";
                    try {
                        email = response.body().string();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (email == null || email == "") {
                        email = "example@example.com";
                    }
                    addDetails(response, email);

                    Toast.makeText(LoginActivity.this, "User logged in!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(LoginActivity.this, LandingActivity.class);
                    intent.putExtra("username", userName);
                    intent.putExtra("token", response.headers().get("Authorization"));
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "Incorrect Credentials! Try again!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void addDetails(Response<ResponseBody> response, String email) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("username", userName);
        editor.putString("password", password);
        editor.putString("token", response.headers().get("Authorization"));
        editor.putString("email", email);
        editor.commit();
    }

    private void checkOnboarding(){
        SharedPreferences preferences =
                getSharedPreferences("my_preferences", MODE_PRIVATE);
        if(!preferences.getBoolean("onboarding_complete", false)){
            Intent intent = new Intent(this, OnboardingActivity.class);
            startActivity(intent);
        }
    }

}