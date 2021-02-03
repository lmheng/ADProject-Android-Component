package sg.edu.iss.mindmatters;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditUserActivity extends BaseActivity {
    private EditText etPassword,etPhone,etEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);
        callCustomActionBar(EditUserActivity.this,false);

        etPassword = findViewById(R.id.etEdPassword);
        etPhone = findViewById(R.id.etEdPhone);
        etEmail = findViewById(R.id.etEdEmail);
        etEmail.setText(getSharedPreferences("user_credentials", MODE_PRIVATE).getString("email","example@example.com"));
        etEmail.setEnabled(false);

        findViewById(R.id.btnEdit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editUser();
            }
        });

    }

    private void editUser() {
        String password = etPassword.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String email = etEmail.getText().toString().trim();

        User u =  new User();
        u.setPassword(password);
        u.setPhone(phone);
        u.setEmail(email);
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getAPI()
                .editUser(getSharedPreferences("user_credentials", MODE_PRIVATE).getString("token",null),u);

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
                    Intent intent = new Intent(EditUserActivity.this, UserMessageActivity.class);
                    intent.putExtra("msg","edited");
                    intent.putExtra("content",s);
                    if(s.contains("Password")) {
                        intent.putExtra("passEdit",true);
                    }
                    startActivity(intent);
                    Toast.makeText(EditUserActivity.this, s, Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(EditUserActivity.this, "Something went wrong!Try again later", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(EditUserActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }
}