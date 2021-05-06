package kawcher.initux.myapplication.Util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import kawcher.initux.myapplication.Model.Server;

public class SharedPrefManager {

    private static SharedPrefManager mInstance;
    private static Context mCtx;

    public   static  String LIST = "clist";
    public   static  String LIST_KEY = "list_key";


    public SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }

        return mInstance;
    }


    public void setList(String b){
        SharedPreferences sharedPrefManager=mCtx.getSharedPreferences(LIST,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPrefManager.edit();
        editor.putString(LIST_KEY,b);
        editor.apply();

    }

    public List<Server> getList(){
        SharedPreferences sharedPrefManager=mCtx.getSharedPreferences(LIST,Context.MODE_PRIVATE);
        String value= sharedPrefManager.getString(LIST_KEY,"null");
        Gson gson = new Gson();
        Type type = new TypeToken<List<Server>>(){}.getType();
        List<Server>ipList = gson.fromJson(value, type);

        return ipList;
    }




}
