package com.blooddonationapp.startactivity.Utils;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blooddonationapp.startactivity.R;
import com.blooddonationapp.startactivity.UserData.bloodBank;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface;
    Context context;
    ArrayList<bloodBank> list;
    private static int mSelectedBloodBank;

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
        holder.distance.setText(new DecimalFormat("##.##" + " km").format(bloodBank.getDistance()/1000));
        FirebaseStorage mStorage = FirebaseStorage.getInstance("gs://blood-donation-applicati-79711.appspot.com/");
        StorageReference ref = mStorage.getReference();
        StorageReference storageRef
                = ref
                .child(
                        "images/"
                                + bloodBank.getName());
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context)
                        .load(uri)
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)         //ALL or NONE as your requirement
                        .thumbnail(Glide.with(context).load(R.drawable.ic_image_loading))
                        .error(R.drawable.ic_image_error)
                        .into(holder.bloodBankImage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Glide.with(context)
                        .load(R.drawable.ic_image_error)
                        .fitCenter()
                        .into(holder.bloodBankImage);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class  MyViewHolder extends RecyclerView.ViewHolder{
        TextView name, address,distance;
        ImageView bloodBankImage;

        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            name = itemView.findViewById(R.id.TVname);
            address = itemView.findViewById(R.id.TVaddress);
            bloodBankImage = itemView.findViewById(R.id.bloodbank_image);
            distance = itemView.findViewById(R.id.TVdistance);


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
