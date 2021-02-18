package sg.edu.iss.mindmatters.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sg.edu.iss.mindmatters.R;
import sg.edu.iss.mindmatters.RetrofitClient;
import sg.edu.iss.mindmatters.activities.fragments.LandingActivity;

public class AppPageActivity extends AppCompatActivity {
    SharedPreferences pref;
    GoogleSignInAccount acct;
    GoogleSignInClient mGoogleSignInClient;
    ProgressBar mProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_page);
         pref = getSharedPreferences(
                "user_credentials", MODE_PRIVATE);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        acct = GoogleSignIn.getLastSignedInAccount(this);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBarAppPage);
        mProgressBar.setIndeterminate(true);

        if (pref.contains("token")) {
            Call<ResponseBody> call = RetrofitClient
                    .getInstance()
                    .getAPI()
                    .verify(getSharedPreferences("user_credentials", MODE_PRIVATE)
                            .getString("token",null));
            mProgressBar.setVisibility(View.VISIBLE);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.code() == 200) {
                        Intent intent = new Intent(AppPageActivity.this, LandingActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        clearDetails();
                    }
                    mProgressBar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                   clearDetails();
                    mProgressBar.setVisibility(View.GONE);
                }
            });
        }
        else{
            Intent intent = new Intent(AppPageActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

    }

    public void clearDetails(){
        if(AccessToken.getCurrentAccessToken()!=null){
            LoginManager.getInstance().logOut();
        }
        if(acct!=null){
            mGoogleSignInClient.signOut();
        }
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
        Intent intent = new Intent(AppPageActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}