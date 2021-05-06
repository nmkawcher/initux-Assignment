package kawcher.initux.myapplication.Api;



import java.util.List;

import kawcher.initux.myapplication.Model.MainResponseModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiResponse {







    @GET("test_app_api.php")
    Call<MainResponseModel>getAllData();






}
