package sg.edu.iss.mindmatters.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.internal.CallbackManagerImpl;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sg.edu.iss.mindmatters.R;
import sg.edu.iss.mindmatters.RetrofitClient;
import sg.edu.iss.mindmatters.activities.fragments.LandingActivity;
import sg.edu.iss.mindmatters.activities.fragments.OnboardingActivity;
import sg.edu.iss.mindmatters.model.User;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private static final int RC_SIGN_IN = 1;
    private static final String TAG = "Google login";
    private EditText etUserEmail, etPassword;
    SharedPreferences pref;
    String userEmail, password;
    GoogleSignInClient mGoogleSignInClient;
    LoginButton loginButton;
    CallbackManager callbackManager;
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        callCustomActionBar(LoginActivity.this, false);
        checkOnboarding();
        loadComponents();
        setupGoogleSignIn();
    }

    private void loadComponents() {
        etUserEmail = findViewById(R.id.etUserEmail);
        etPassword = findViewById(R.id.etPassword);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBarLogin);
        mProgressBar.setIndeterminate(true);
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
        findViewById(R.id.fbLoginButton).setOnClickListener(this);

        //Facebook Login Button
        loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(this);
    }


    private void setupGoogleSignIn() {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        findViewById(R.id.sign_in_button).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnLogin) {
            userEmail = etUserEmail.getText().toString().trim();
            password = etPassword.getText().toString();
            loginUser(userEmail, password);
        } else if (id == R.id.tvWoLogin) {
            Intent intent = new Intent(LoginActivity.this, LandingActivity.class);
            startActivity(intent);
        } else if (id == R.id.tvRegisterLink) {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        } else if (id == R.id.forgotLink) {
            startActivity(new Intent(LoginActivity.this, ForgotPwdActivity.class));
        } else if (id == R.id.sign_in_button) {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        } else if (id == R.id.login_button) {
            facebookSignin();
        }else if(id==R.id.fbLoginButton){
            loginButton.performClick();
        }
    }

    private boolean validate(String userEmail, String password) {
        if (userEmail.isEmpty()) {
            etUserEmail.setError("Username is required");
            etUserEmail.requestFocus();
            return false;
        } else if (password.isEmpty()) {
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return false;
        }
        return true;
    }

    private void loginUser(String userEmail, String password) {
        if (!validate(userEmail, password)) {
            return;
        }
        User u = new User();
        u.setEmail(userEmail);
        u.setPassword(password);
        Call<User> call = RetrofitClient
                .getInstance()
                .getAPI()
                .checkUser(u);
        callRetrofit(call,"email");
    }

    private void addDetails(Response<User> response, String loginMethod) {
        User responseUser = null;
        try {
            responseUser = response.body();
            System.out.println("username in response is" + responseUser.getUserName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(responseUser!=null) {
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("username", responseUser.getUserName());
            editor.putString("token", response.headers().get("Authorization"));
            editor.putString("loginMethod",loginMethod);
            editor.commit();
            Toast.makeText(LoginActivity.this, "User logged in!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(LoginActivity.this, LandingActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }

        if (requestCode == CallbackManagerImpl.RequestCodeOffset.Login.toRequestCode()) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String accessToken = account.getIdToken();
            User user = new User();
            user.setAccessToken(accessToken);
            user.setLoginMethod("google");
            Call<User> call = RetrofitClient
                    .getInstance()
                    .getAPI()
                    .loginSocial(user);
            callRetrofit(call,"google");
        } catch (ApiException e) {
            // The ApiException status code indicates
            // the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(LoginActivity.this, "Something went wrong! Try again later", Toast.LENGTH_LONG).show();
        }
    }

    private void facebookSignin() {
        loginButton.setPermissions(Arrays.asList("email", "public_profile"));
        callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //Server call to authenticate at backend
                User user = new User();
                user.setAccessToken(AccessToken.getCurrentAccessToken().getToken());
                user.setLoginMethod("facebook");
                Call<User> call = RetrofitClient
                        .getInstance()
                        .getAPI()
                        .loginSocial(user);
                callRetrofit(call,"facebook");
            }

            @Override
            public void onCancel() {
                // App code
                Log.d("Error", "Cancelled");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                exception.printStackTrace();
                Log.d("Error", "Error during Login");
            }
        });
    }

    public void callRetrofit(Call<User> call, String loginMethod){
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        mProgressBar.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 200) {
                    addDetails(response,loginMethod);
                    finish();
                } else {
                    signOut();
                    String message = "Incorrect Credentials! Try again!";
                    if(response.headers().get("Error")!=null){
                        message = response.headers().get("Error");
                    }
                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
                }
                mProgressBar.setVisibility(View.GONE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                signOut();
                Toast.makeText(LoginActivity.this, "Something went wrong! Try again later", Toast.LENGTH_LONG).show();
                mProgressBar.setVisibility(View.GONE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        });
    }

    public void signOut(){
        disconnectFromFacebook();
        disconnectFromGoogle();
    }


    public void disconnectFromFacebook(){
        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }
        LoginManager.getInstance().logOut();
  }

    public void disconnectFromGoogle(){
        mGoogleSignInClient.signOut();
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