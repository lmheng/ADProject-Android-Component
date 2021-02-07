package sg.edu.iss.mindmatters;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface API {
    @POST("users/register")
    Call<ResponseBody> createUser (
            @Body User user
    );

    @POST("users/edit")
    Call<ResponseBody> editUser (
            @Header("Authorization") String authorization,@Body User user
    );

    @POST("login")
    Call<ResponseBody> checkUser (
            @Body User user
    );

    @GET("users/success")
    Call<ResponseBody> getSuccess(@Header("Authorization") String authorization);


    @GET("rest/profile/{uName}")
    Call<QuizOutcome> getUserProfile(@Path("uName")String username);

    @GET("rest/list")
    Call<List<Resource>> getallresources();
}
