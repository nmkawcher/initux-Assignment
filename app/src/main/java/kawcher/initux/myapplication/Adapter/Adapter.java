package kawcher.initux.myapplication.Adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


import kawcher.initux.myapplication.R;
import kawcher.initux.myapplication.View.MainActivity;

import static android.content.Context.CLIPBOARD_SERVICE;

public class Adapter extends RecyclerView.Adapter<Adapter.MyHolder> {

    List<MainActivity.Server>list;
    Context context;
    private ClipboardManager clipboardManager;
    private ClipData clipData;

    public Adapter(List<MainActivity.Server> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.rv_item_layout,parent,false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        MainActivity.Server server=list.get(position);
        holder.ipTV.setText(server.getVpnIp());
        holder.passwordTV.setText(server.getPassword());

        holder.ipIv.setOnClickListener(v -> {
            String ip=list.get(0).getVpnIp();
           copyDataToClipBoard(ip);
        });

        holder.passIv.setOnClickListener(v -> {
            String password=list.get(0).getPassword();
            copyDataToClipBoard(password);

        });

    }

    private void copyDataToClipBoard(String text){

       clipboardManager=(ClipboardManager)context.getSystemService(CLIPBOARD_SERVICE);
        clipData = ClipData.newPlainText("text",text);
        clipboardManager.setPrimaryClip(clipData);
    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{

        private TextView ipTV,passwordTV;
        private ImageView ipIv,passIv;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            ipTV=itemView.findViewById(R.id.server_ip);
            passwordTV=itemView.findViewById(R.id.password);
            ipIv=itemView.findViewById(R.id.iv_ip_copy);
            passIv=itemView.findViewById(R.id.iv_pass_copy);
        }
    }
}
