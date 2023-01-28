package com.blooddonationapp.startactivity.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blooddonationapp.startactivity.R;
import com.blooddonationapp.startactivity.UserData.Request;

import java.util.ArrayList;

public class CardView_RequestAdapter extends RecyclerView.Adapter<CardView_RequestAdapter.requestViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface;


    private Context context;
    ArrayList<Request> list = new ArrayList<>();

    public CardView_RequestAdapter(Context ctx, RecyclerViewInterface recyclerViewInterface) {
        this.context = ctx;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    public void setItems(ArrayList<Request> requests) {
        list.addAll(requests);
    }

    @NonNull
    @Override
    public requestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.request_cardlayout, parent, false);
        return new requestViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull requestViewHolder holder, int position) {
        requestViewHolder vh = (requestViewHolder) holder;
        Request request = list.get(position);
        vh.name.setText(request.getName());
        vh.date.setText(request.getDate());
        vh.time.setText(request.getTime());
        vh.status.setText(request.getStatus());

    }


    @Override
    public int getItemCount() {

        return list.size();
    }

    public static class requestViewHolder extends RecyclerView.ViewHolder {


        public TextView name, date, time, status;

        public requestViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            name = itemView.findViewById(R.id.RequestFragment_textView_name);
            date = itemView.findViewById(R.id.RequestFragment_textView_date);
            time = itemView.findViewById(R.id.RequestFragment_textView_time);
            status = itemView.findViewById(R.id.RequestFragment_textView_status);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recyclerViewInterface != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION)
                            recyclerViewInterface.onItemClick(position);

                    }
                }
            });

        }


    }
}
