package sg.edu.iss.mindmatters.activities;

import android.content.Intent;
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
import sg.edu.iss.mindmatters.R;
import sg.edu.iss.mindmatters.RetrofitClient;
import sg.edu.iss.mindmatters.model.User;

public class RegisterActivity extends BaseActivity implements View.OnClickListener{

    private EditText etUsername, etPassword,etPhone,etEmail,etRePassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        callCustomActionBar(RegisterActivity.this,false);
        loadComponents();

    }


    private void loadComponents(){
        etUsername = findViewById(R.id.etRUserName);
        etPassword = findViewById(R.id.etRPassword);
        etRePassword = findViewById(R.id.etReenterPassword);
        etPhone = findViewById(R.id.etRPhone);
        etEmail = findViewById(R.id.etREmail);

        findViewById(R.id.btnRegister).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id==R.id.btnRegister){
            registerUser();
        }
    }

    private boolean validate(String userName,String password,String rePassword,String phone,String email)
    {
        if (userName.isEmpty()) {
            etUsername.setError("Username cannot be blank");
            etUsername.requestFocus();
            return false;
        }
        else if (password.isEmpty()) {
            etPassword.setError("Password cannot be blank");
            etPassword.requestFocus();
            return false;
        }
        else if (rePassword.isEmpty()) {
            etRePassword.setError("Password cannot be blank");
            etRePassword.requestFocus();
            return false;
        }
        else if (email.isEmpty()) {
            etEmail.setError("Email cannot be blank");
            etEmail.requestFocus();
            return false;
        }
        else if(!userName.matches("^[A-Za-z0-9]*$")){
            etUsername.setError("Username can contain only  alphabets and numbers");
            etUsername.requestFocus();
            return false;
        } else if(!password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@$!%*#?&]).{8,}$")){
            etPassword.setError("At least 8 characters. Should be combination of numbers, uppercase, lowercase and one special " +
                    "character from @$!%*#?&");
            etPassword.requestFocus();
            return false;
        }
        else if (!password.equals(rePassword)) {
            etPassword.setError("Passwords do not match");
            etRePassword.setError("Passwords do not match");
            etPassword.requestFocus();
            return false;
        }else if(!phone.matches("^[+]?[0-9]{8,20}$") && !phone.isEmpty()){
            etPhone.setError("Phone number can contain only numbers and + before country codes");
            etPhone.requestFocus();
            return false;
        }
        else if (!email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
            etEmail.setError("Email address should be of format example@example.com");
            etEmail.requestFocus();
            return false;
        }
        return true;
    }
    private void registerUser() {
        String userName = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String rePassword = etRePassword.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String email = etEmail.getText().toString().trim();

        if(!validate(userName,password,rePassword,phone,email)){
            return;
        }

        User u =  new User();
        u.setUserName(userName);
        u.setPassword(password);
        u.setEmail(email);
        u.setPhone(phone);
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getAPI()
                .createUser(u);

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
                    if(s.contains("SUCCESS")) {
                        Intent intent = new Intent(RegisterActivity.this, UserMessageActivity.class);
                        intent.putExtra("msg","registered");
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(RegisterActivity.this, s, Toast.LENGTH_LONG).show();
                    }
                } else {
                    try{
                        String str=response.errorBody().string();
                        JSONObject json = new JSONObject(str);
                        if(json.has("userName")){
                            etUsername.setError(json.get("userName").toString());
                            etUsername.requestFocus();
                            return;
                        }
                        else if(json.has("password")){
                            etPassword.setError(json.get("password").toString());
                            etPassword.requestFocus();
                            return;
                        }
                        else if(json.has("phone")){
                            etPhone.setError(json.get("phone").toString());
                            etPhone.requestFocus();
                            return;
                        }
                        else if(json.has("email")){
                            etEmail.setError(json.get("email").toString());
                            etEmail.requestFocus();
                            return;
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
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