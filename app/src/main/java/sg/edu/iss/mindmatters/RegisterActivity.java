package sg.edu.iss.mindmatters;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends ParentActivity {

    private EditText etUsername, etPassword,etPhone,etEmail,etFirstName,etLastName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        callCustomActionBar();

        etUsername = findViewById(R.id.etRUserName);
        etPassword = findViewById(R.id.etRPassword);
        etPhone = findViewById(R.id.etRPhone);
        etEmail = findViewById(R.id.etREmail);
        etFirstName = findViewById(R.id.etRFirstName);
        etLastName = findViewById(R.id.etRLastName);

        findViewById(R.id.btnRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });


    }

    private void registerUser() {
        String userName = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();

        if (userName.isEmpty()) {
            etUsername.setError("Username is required");
            etUsername.requestFocus();
            return;
        } else if (password.isEmpty()) {
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return;
        }

        User u =  new User();
        u.setUserName(userName);
        u.setPassword(password);
        u.setEmail(email);
        u.setFirstName(firstName);
        u.setLastName(lastName);
        u.setPhone(phone);
        Call<ResponseBody> call = RetrofitClient
                    .getInstance()
                    .getAPI()
                    .createUser(u);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("rcode", String.valueOf(response.code()));
                if (response.code() == 200) {
                    String s="";
                    try{
                        s= response.body().string();
                    }
                    catch (Exception e){
                        throw new RuntimeException();
                    }
                    if(s.contains("SUCCESS")) {
                        Toast.makeText(RegisterActivity.this, s, Toast.LENGTH_LONG).show();
                        startActivity(new Intent(RegisterActivity.this, VerifyAccountActivity.class));
                    }
                    else{
                        Toast.makeText(RegisterActivity.this, s, Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "Something went wrong!Try again later", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

}