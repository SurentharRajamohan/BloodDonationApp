package com.blooddonationapp.startactivity.Utils;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blooddonationapp.startactivity.R;

public class bloodBankViewHolder extends RecyclerView.ViewHolder {

    // here is where the card views get their items
    public TextView bloodBankName, bloodBankDate, bloodBankTime;
    public ImageView bloodTypeImage;

    public bloodBankViewHolder(@NonNull View itemView) {
        super(itemView);
        bloodBankName = itemView.findViewById(R.id.recyclerView_textView_bloodBankName);
        bloodBankDate = itemView.findViewById(R.id.recyclerView_textView_bloodBankDate);
        bloodBankTime = itemView.findViewById(R.id.recyclerView_textView_bloodBankTime);
        bloodTypeImage = itemView.findViewById(R.id.recyclerView_image_bloodType);
    }
}
