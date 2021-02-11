package sg.edu.iss.mindmatters;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditUserActivity extends BaseActivity implements View.OnClickListener{

    private EditText etPassword,etPhone,etEmail,etRePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);
        callCustomActionBar(EditUserActivity.this,false);
        loadComponents();
    }

    private void loadComponents(){
        etPassword = findViewById(R.id.etEdPassword);
        etPhone = findViewById(R.id.etEdPhone);
        etEmail = findViewById(R.id.etEdEmail);
        etRePassword = findViewById(R.id.etEdReenterPassword);
        etEmail.setText(getSharedPreferences("user_credentials", MODE_PRIVATE).getString("email","example@example.com"));
        etEmail.setEnabled(false);

        findViewById(R.id.btnEdit).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id==R.id.btnEdit){
            editUser();
        }
    }

    private boolean validate(String password, String phone, String rePassword, String email){
        if (password.isEmpty()) {
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return false;
        } else if (rePassword.isEmpty()) {
            etRePassword.setError("Password is required");
            etRePassword.requestFocus();
            return false;
        } else if(!password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@$!%*#?&]).{8,}$")){
            etPassword.setError("At least 8 characters. Should be combination of numbers, uppercase, " +
                    "lowercase and one special character from @$!%*#?&");
            etPassword.requestFocus();
            return false;
        }else if (!password.equals(rePassword)) {
            etPassword.setError("Passwords do not match");
            etRePassword.setError("Passwords do not match");
            etPassword.requestFocus();
            return false;
        }
        else if(!phone.matches("^[+]?[0-9]{8,20}$")){
            etPhone.setError("Phone number can contain only numbers and + before country codes");
            etPhone.requestFocus();
            return false;
        }
        return true;
    }

    private void editUser() {
        String password = etPassword.getText().toString();
        String phone = etPhone.getText().toString().trim();
        String rePassword = etRePassword.getText().toString();
        String email = etEmail.getText().toString().trim();

        if(!validate(password,phone,rePassword,email)){
            return;
        }

        User u =  new User();
        u.setPassword(password);
        u.setPhone(phone);
        u.setEmail(email);

        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getAPI()
                .editUser(getSharedPreferences("user_credentials", MODE_PRIVATE)
                        .getString("token",null),u);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    String s="";
                    try{
                        s= response.body().string();
                    }
                    catch (Exception e){
                        e.printStackTrace();
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
                    try{
                        String str= response.errorBody().string();
                        JSONObject json = new JSONObject(str);
                        if(json.has("password")){
                            etPassword.setError(json.get("password").toString());
                            etPassword.requestFocus();
                            return;
                        }
                        else if(json.has("phone")){
                            etPhone.setError(json.get("phone").toString());
                            etPhone.requestFocus();
                            return;
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
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