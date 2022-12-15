package com.blooddonationapp.startactivity.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blooddonationapp.startactivity.R;
import com.blooddonationapp.startactivity.UserData.bloodBank;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface;
    Context context;
    ArrayList<bloodBank> list;

    public MyAdapter(Context context, ArrayList<bloodBank> list,
                     RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.list = list;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    public void filterList(ArrayList<bloodBank> filterList){
        list = filterList;
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.list_layout,parent,false);
        return new MyViewHolder(v, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        bloodBank bloodBank = list.get(position);
        holder.name.setText(bloodBank.getName());
        holder.address.setText(bloodBank.getAddress());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class  MyViewHolder extends RecyclerView.ViewHolder{
        TextView name, address;

        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            name = itemView.findViewById(R.id.TVname);
            address = itemView.findViewById(R.id.TVaddress);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(recyclerViewInterface !=null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION)
                            recyclerViewInterface.onItemClick(position);

                    }
                }
            });
        }
    }
}
