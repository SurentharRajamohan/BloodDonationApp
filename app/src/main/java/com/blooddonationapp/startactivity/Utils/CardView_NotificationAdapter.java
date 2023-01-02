package com.blooddonationapp.startactivity.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blooddonationapp.startactivity.R;
import com.blooddonationapp.startactivity.UserData.Notification;
import com.blooddonationapp.startactivity.UserData.Request;

import java.util.ArrayList;

public class CardView_NotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    ArrayList<Notification> list = new ArrayList<>();

    public CardView_NotificationAdapter(Context ctx){
        this.context = ctx;
    }
    public void setItems(ArrayList<Notification> notifications){
        list.addAll(notifications);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notification_cardlayout, parent, false);
        return new notificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        notificationViewHolder vh = (notificationViewHolder) holder;
        Notification notification = list.get(position);
        vh.title.setText(notification.getTitle());
        vh.date.setText(notification.getDate());
        vh.description.setText(notification.getDescription());



    }

    @Override
    public int getItemCount() {

        return list.size();
    }
}
