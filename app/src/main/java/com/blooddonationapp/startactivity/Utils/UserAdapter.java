package com.blooddonationapp.startactivity.Utils;

import android.content.Context;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blooddonationapp.startactivity.DisplayBloodBank;
import com.blooddonationapp.startactivity.R;
import com.blooddonationapp.startactivity.UserData.User;
import com.blooddonationapp.startactivity.UserData.bloodBank;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface;
    Context context;
    ArrayList<User> list;
    Bundle bundle;

    public UserAdapter(Context context, ArrayList<User> list,
                       RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.list = list;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    public void filterList(ArrayList<User> filterList){
        list = filterList;
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.user_layout,parent,false);
        return new MyViewHolder(v, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        User user = list.get(position);
        holder.name.setText(user.getFirstName());
        holder.bloodtype.setText(user.getBloodGroup());
        holder.distance.setText(new DecimalFormat("##.##" + " km").format(user.getDistance()));
//        Glide.with(holder.donorImage.getContext()).load(user.getImage()).into(holder.donorImage);
        FirebaseStorage mStorage = FirebaseStorage.getInstance("gs://blood-donation-applicati-79711.appspot.com/");
        StorageReference ref = mStorage.getReference();
        StorageReference storageRef
                = ref
                .child(
                        "images/"
                                + user.getFirstName());
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context)
                        .load(uri)
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)         //ALL or NONE as your requirement
                        .thumbnail(Glide.with(context).load(R.drawable.ic_image_loading))
                        .error(R.drawable.ic_image_error)
                        .into(holder.donorImage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Glide.with(context)
                        .load(R.drawable.ic_image_error)
                        .fitCenter()
                        .into(holder.donorImage);
            }
        });



//        holder.distance.setText((int) getDistance(user,bbLatitude,bbLongitude));
//        holder.address.setText(bloodBank.getAddress());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class  MyViewHolder extends RecyclerView.ViewHolder{
        TextView name,bloodtype, distance;
        ImageView donorImage;

        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            name = itemView.findViewById(R.id.TVUsername);
            bloodtype = itemView.findViewById(R.id.TVBloodType);
            donorImage = itemView.findViewById(R.id.profile_image);
            distance = itemView.findViewById(R.id.distance);


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
