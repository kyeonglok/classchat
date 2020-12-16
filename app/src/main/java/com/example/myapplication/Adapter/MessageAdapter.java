package com.example.myapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.ReportActivity;
import com.example.myapplication.Usermodel.Chat;
import com.example.myapplication.Usermodel.Users;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder>{
    private Context context;
    private List<Chat> mChats;
    private String img_url;
    FirebaseUser fuser;
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    public MessageAdapter(Context ctx, List<Chat> Chats, String img) {
        context = ctx;
        mChats = Chats;
        img_url = img;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView showMessage,nickname,chattime,participant,msgviewtime;

        public ViewHolder(View itemView){
            super(itemView);
            showMessage = itemView.findViewById(R.id.show_message);
            nickname = itemView.findViewById(R.id.nickname);
            chattime = itemView.findViewById(R.id.chat_time);
            msgviewtime = itemView.findViewById(R.id.msg_view_time);
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if(viewType == MSG_TYPE_RIGHT)
            view = LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent, false);
        else
            view = LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Chat chat = mChats.get(position);
        holder.showMessage.setText(chat.getMsg());
        holder.nickname.setText(chat.getSender_nick());
        holder.chattime.setText(chat.getTime());
        int num = Integer.parseInt(chat.getView_time());
        if(num > 0)
            holder.msgviewtime.setText(chat.getView_time());
        else
            holder.msgviewtime.setText("");
        holder.showMessage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent i = new Intent(context, ReportActivity.class);
                i.putExtra("id",chat.getSender_id());
                i.putExtra("msg",chat.getMsg());
                i.putExtra("time",chat.getTime());
                context.startActivity(i);
                return true;
            }
        });

    }

    @Override
    public int getItemViewType(int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if(mChats.get(position).getSender_id().equals(fuser.getEmail())) return MSG_TYPE_RIGHT;
        else return MSG_TYPE_LEFT;
    }

    @Override
    public int getItemCount() {
        return mChats.size();
    }
}
