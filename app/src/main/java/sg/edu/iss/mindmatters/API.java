package sg.edu.iss.mindmatters;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import sg.edu.iss.mindmatters.model.QuizOutcome;
import sg.edu.iss.mindmatters.model.Resource;
import sg.edu.iss.mindmatters.model.User;

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

    @GET("rest/profile/{uName}")
    Call<QuizOutcome> getUserProfile(@Path("uName")String username);

    @GET("rest/list")
    Call<List<Resource>> getallresources();

    @POST("users/forgot")
    Call<ResponseBody> resetPassword(
            @Body User user
    );

}
