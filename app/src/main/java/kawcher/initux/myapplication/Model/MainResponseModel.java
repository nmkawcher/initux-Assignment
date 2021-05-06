package kawcher.initux.myapplication.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MainResponseModel {


    @SerializedName("Date")
    @Expose
    private String date;
    @SerializedName("Servers")
    @Expose
    private List<Server> servers = null;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Server> getServers() {
        return servers;
    }

    public void setServers(List<Server> servers) {
        this.servers = servers;
    }


}


