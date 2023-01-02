package com.blooddonationapp.startactivity.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blooddonationapp.startactivity.R;
import com.blooddonationapp.startactivity.UserData.Request;

import java.util.ArrayList;

public class CardView_RequestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    ArrayList<Request> list = new ArrayList<>();

    public CardView_RequestAdapter(Context ctx){
        this.context = ctx;
    }
    public void setItems(ArrayList<Request> requests){
        list.addAll(requests);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.request_cardlayout, parent, false);
        return new requestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
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
}
