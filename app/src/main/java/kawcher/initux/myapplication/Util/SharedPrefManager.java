package kawcher.initux.myapplication.Util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


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


    public String getList()  {
        SharedPreferences sharedPrefManager=mCtx.getSharedPreferences(LIST,Context.MODE_PRIVATE);
        String value= sharedPrefManager.getString(LIST_KEY,"null");


        return value;
    }



}
