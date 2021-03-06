package kawcher.initux.myapplication.View;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import kawcher.initux.myapplication.Adapter.Adapter;
import kawcher.initux.myapplication.Api.ApiResponse;
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
    private List<MainActivity.Server>ipList;
    private Adapter adapter;
    private SharedPrefManager sharedPrefManager;
    private Button createFileBtn,createFolderBtn,deleteFileBtn,deleteFolderBtn;
    private File filePath = new File(Environment.getExternalStorageDirectory() + "/ABC.txt");
    private String folderDirectory="/sdcard/new_dir";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE
                , Manifest.permission.READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);



        init();
        loadData();
        onClickListener();



    }





    private void loadRVData() throws JSONException {


       String value=sharedPrefManager.getList();
        JSONObject jsonObject = new JSONObject(value);


        ipList.clear();

        JSONArray jsonArray = jsonObject.getJSONArray("Servers");
        for (int i = 0; i < jsonArray.length(); i++) {
            String vpnIp = jsonArray.getJSONObject(i).getString("VpnIp");
            String password = jsonArray.getJSONObject(i).getString("Password");
            String proxyIp = jsonArray.getJSONObject(i).getString("ProxyIp");


            Server server=new Server(vpnIp,proxyIp,password);
           ipList.add(server);

        }
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


        Call<JsonObject> call = apiResponse.getAllData();

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if(response.isSuccessful()){


                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(String.valueOf(response.body()));
                        sharedPrefManager.setList(jsonObject.toString());
                        loadRVData();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }





                }
            }



            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(MainActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("TAG", "onFailure: "+t.getMessage() );

            }
        });
    }

    private void init() {
        ipList=new ArrayList<>();
        recyclerView=findViewById(R.id.rv);
        sharedPrefManager=SharedPrefManager.getInstance(MainActivity.this);
        createFileBtn=findViewById(R.id.create_file_button);
        createFolderBtn=findViewById(R.id.create_folder_button);
        deleteFileBtn=findViewById(R.id.delete_file_button);
        deleteFolderBtn=findViewById(R.id.delete_folder_button);
    }

    private void onClickListener() {

        createFileBtn.setOnClickListener(v -> createFile());

        deleteFileBtn.setOnClickListener(v -> {
            if (filePath.exists()){
                filePath.delete();
                Toast.makeText(MainActivity.this, "file deleted", Toast.LENGTH_SHORT).show();
            }
        });

        createFolderBtn.setOnClickListener(v -> {
            File dir = new File(folderDirectory);
            try{
                if(dir.mkdir()) {
                    Toast.makeText(this, "Directory created", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Directory is not created", Toast.LENGTH_SHORT).show();
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        });

        deleteFolderBtn.setOnClickListener(v ->{

            File del = new File("/sdcard/new_dir");
            boolean success = del.delete();
            if (!success) {
                Toast.makeText(this, "Deletion of directory failed!", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "Deletion of directory successful", Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void createFile() {
        try {
            filePath.createNewFile();
            FileOutputStream stream = new FileOutputStream(filePath);
            try {
                stream.write("text-to-write".getBytes());
            } finally {
                stream.close();
            }
            Toast.makeText(this, "File Created Successfully!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class Server {

        private String vpnIp;
        private String proxyIp;
        private String password;


        public Server(String vpnIp, String proxyIp, String password) {
            this.vpnIp = vpnIp;
            this.proxyIp = proxyIp;
            this.password = password;
        }

        public String getVpnIp() {
            return vpnIp;
        }

        public void setVpnIp(String vpnIp) {
            this.vpnIp = vpnIp;
        }

        public String getProxyIp() {
            return proxyIp;
        }

        public void setProxyIp(String proxyIp) {
            this.proxyIp = proxyIp;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }



}