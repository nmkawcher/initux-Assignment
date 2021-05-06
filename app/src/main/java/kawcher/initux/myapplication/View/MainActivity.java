package kawcher.initux.myapplication.View;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import kawcher.initux.myapplication.Adapter.Adapter;
import kawcher.initux.myapplication.Api.ApiResponse;
import kawcher.initux.myapplication.Model.MainResponseModel;
import kawcher.initux.myapplication.Model.Server;
import kawcher.initux.myapplication.R;
import kawcher.initux.myapplication.Util.SharedPrefManager;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Server>ipList;
    private Adapter adapter;
    private SharedPrefManager sharedPrefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        loadData();



    }

    private void loadRVData() {

       ipList=sharedPrefManager.getList();
        LinearLayoutManager llm=new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(llm);
        adapter=new Adapter(ipList,MainActivity.this);
        recyclerView.setAdapter(adapter);
    }

    private void loadData() {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(40, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://31.207.44.163/api/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();

        ApiResponse apiResponse = retrofit.create(ApiResponse.class);


        Call<MainResponseModel> call = apiResponse.getAllData();

        call.enqueue(new Callback<MainResponseModel>() {
            @Override
            public void onResponse(Call<MainResponseModel> call, Response<MainResponseModel> response) {

                if(response.isSuccessful()){

                    Gson gson = new Gson();
                    String json = gson.toJson(response.body().getServers());
                    sharedPrefManager.setList(json);
                   loadRVData();

                }
            }

            @Override
            public void onFailure(Call<MainResponseModel> call, Throwable t) {
                Toast.makeText(MainActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("TAG", "onFailure: "+t.getMessage() );

            }
        });
    }

    private void init() {
        recyclerView=findViewById(R.id.rv);
        sharedPrefManager=SharedPrefManager.getInstance(MainActivity.this);
    }
}