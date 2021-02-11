package sg.edu.iss.mindmatters;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPwdActivity extends BaseActivity implements View.OnClickListener {

    private EditText etEmail;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callCustomActionBar(ForgotPwdActivity.this, false);
        setContentView(R.layout.activity_forgot_pwd);
        findViewById(R.id.btnForgot).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnForgot) {
            etEmail = (EditText) findViewById(R.id.etRegEmail);
            email = etEmail.getText().toString().trim();
            resetPassword(email);
        }
    }

    private boolean validate(String email) {
        if (email.isEmpty()) {
            etEmail.setError("Email cannot be blank");
            etEmail.requestFocus();
            return false;
        } else if (!email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
            etEmail.setError("Email address should be of format example@example.com");
            etEmail.requestFocus();
            return false;
        }
        return true;
    }

    private void resetPassword(String email) {
        if (!validate(email)) {
            return;
        }

        User user = new User();
        user.setEmail(email);

        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getAPI()
                .resetPassword(user);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    String s = "";
                    try {
                        s = response.body().string();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (s.contains("SUCCESS")) {
                        Intent intent = new Intent(ForgotPwdActivity.this, UserMessageActivity.class);
                        intent.putExtra("msg", "resetPwd");
                        intent.putExtra("content", s);
                        startActivity(intent);
                    }
                    Toast.makeText(ForgotPwdActivity.this, s, Toast.LENGTH_LONG).show();

                } else {
                    try {
                        String str = "";
                        str = response.errorBody().string();
                        System.out.println("response is " + str);
                        JSONObject json = new JSONObject(str);
                        if (json.has("email")) {
                            etEmail.setError(json.get("email").toString());
                            etEmail.requestFocus();
                            return;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(ForgotPwdActivity.this, "Something went wrong!Try again later", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ForgotPwdActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

}