package kawcher.initux.myapplication.Api;



import com.google.gson.JsonObject;

import java.util.List;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiResponse {







    @GET("test_app_api.php")
    Call<JsonObject>getAllData();






}
