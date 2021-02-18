package sg.edu.iss.mindmatters.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
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
import sg.edu.iss.mindmatters.model.User;

public class DeleteActivity extends BaseActivity implements View.OnClickListener {

    GoogleSignInClient mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        callCustomActionBar(DeleteActivity.this, false);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        SharedPreferences pref = getSharedPreferences(
                "user_credentials", MODE_PRIVATE);
        String loginMethod = pref.getString("loginMethod", "none");
        if (loginMethod.equals("email")) {
            findViewById(R.id.deleteWithEmail).setVisibility(View.VISIBLE);
            findViewById(R.id.btnDelete).setOnClickListener(this);
        } else if (loginMethod.equals("facebook")) {
            findViewById(R.id.socialDeleteLayout).setVisibility(View.VISIBLE);
            findViewById(R.id.fbDeleteButton).setVisibility(View.VISIBLE);
            findViewById(R.id.fbDeleteButton).setOnClickListener(this);
        } else if (loginMethod.equals("google")) {
            findViewById(R.id.socialDeleteLayout).setVisibility(View.VISIBLE);
            findViewById(R.id.delete_sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.delete_sign_in_button).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnDelete) {
            EditText etPassword = (EditText) findViewById(R.id.etDelPassword);
            String password = etPassword.getText().toString();
            if (password.isEmpty()) {
                etPassword.setError("Password cannot be blank");
            }
            deleteAccount(password, "email");
        } else if (id == R.id.fbDeleteButton) {
            String accessToken = "";
            if (AccessToken.getCurrentAccessToken() != null) {
                accessToken = AccessToken.getCurrentAccessToken().getToken();
            }
            deleteAccount(accessToken, "facebook");
        } else if (id == R.id.delete_sign_in_button) {
            String accessToken = "";
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
            if (acct != null) {
                accessToken = acct.getIdToken();
            }
            deleteAccount(accessToken, "google");
        }
    }

    private void deleteAccount(String token, String loginMethod) {
        User user = new User();
        if (loginMethod.equals("email")) {
            user.setPassword(token);
        } else {
            user.setAccessToken(token);
        }
        user.setLoginMethod(loginMethod);
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getAPI()
                .deleteAccount(getSharedPreferences("user_credentials", MODE_PRIVATE)
                        .getString("token", null), user);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    if (loginMethod.equals("facebook")) {
                        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                                .Callback() {
                            @Override
                            public void onCompleted(GraphResponse graphResponse) {

                                LoginManager.getInstance().logOut();

                            }
                        }).executeAsync();
                    }
                    if (loginMethod.equals("google")) {
                        mGoogleSignInClient.signOut();
                    }
                    Intent intent = new Intent(DeleteActivity.this, UserMessageActivity.class);
                    intent.putExtra("msg", "deleted");
                    startActivity(intent);
                } else {
                    Toast.makeText(DeleteActivity.this, "Invalid credentials!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(DeleteActivity.this, "Something went wrong!Try again later", Toast.LENGTH_LONG).show();
            }
        });

    }
}