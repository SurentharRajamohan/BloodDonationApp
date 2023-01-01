package com.blooddonationapp.startactivity.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blooddonationapp.startactivity.R;
import com.blooddonationapp.startactivity.UserData.bloodBank;


import java.util.ArrayList;

public class CardView_RVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    ArrayList<bloodBank> list = new ArrayList<>();

    public CardView_RVAdapter(Context ctx){
        this.context = ctx;
    }

    public void setItems(ArrayList<bloodBank> bloodBanks){
        list.addAll(bloodBanks);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_cardlayout, parent, false);
        return new bloodBankViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        bloodBankViewHolder vh = (bloodBankViewHolder) holder;
        bloodBank bloodBankObj = list.get(position);
        vh.bloodBankName.setText(bloodBankObj.getName());
        vh.bloodBankDate.setText(bloodBankObj.getDate());
        vh.bloodBankTime.setText(bloodBankObj.getTime());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
