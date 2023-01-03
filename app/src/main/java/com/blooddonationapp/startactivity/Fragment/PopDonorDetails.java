package com.blooddonationapp.startactivity.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.blooddonationapp.startactivity.R;

public class PopDonorDetails extends Activity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.popdonordetailswindow);

        DisplayMetrics dn= new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dn);

        int width=dn.widthPixels;
        int height=dn.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.6));

        Button btn= findViewById(R.id.requestbutton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent= new Intent(PopDonorDetails.this,PopSuccessful.class);
                startActivity(intent);

            }
        });
    }


}
