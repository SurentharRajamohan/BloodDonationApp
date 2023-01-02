package com.blooddonationapp.startactivity.Utils;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blooddonationapp.startactivity.R;

public class notificationViewHolder extends RecyclerView.ViewHolder{

    public TextView title,date,description;

    public notificationViewHolder(@NonNull View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.NotificationFragment_textView_title);
        date = itemView.findViewById(R.id.NotificationFragment_textView_date);
        description = itemView.findViewById(R.id.NotificationFragment_textView_description);


    }
}
