package com.example.myapplication.Adapter;



import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.MessageActivity;
import com.example.myapplication.R;
import com.example.myapplication.Usermodel.Users;

import java.util.List;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ViewHolder> {
    private List<Users> mUsers;
    private Context ctx;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View classView = inflater.inflate(R.layout.user_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(classView);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Users user = mUsers.get(position);
        TextView tv = holder.classname;
        tv.setText(user.getNickname());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ctx, MessageActivity.class);
                String strarr[] = new String[2];
                strarr[0] = user.getNickname();
                strarr[1] = user.getProfileURL();
                i.putExtra("class",strarr);
                ctx.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView classname;
        public TextView recent_msg;
        public ViewHolder(View itemView){
            super(itemView);
            recent_msg = itemView.findViewById(R.id.final_msg_time);
            classname = itemView.findViewById(R.id.textViewprofile);
        }
    }

    public ClassAdapter(Context context,List<Users> users){
        ctx = context;
        mUsers = users;
    }


}

