package sg.edu.iss.mindmatters;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface API {
    @POST("users/register")
    Call<ResponseBody> createUser(
            @Body User user
    );

    @POST("users/edit")
    Call<ResponseBody> editUser(
            @Header("Authorization") String authorization, @Body User user
    );

    @POST("login")
    Call<ResponseBody> checkUser(
            @Body User user
    );

    @POST("users/forgot")
    Call<ResponseBody> resetPassword(
      @Body User user
    );
}
