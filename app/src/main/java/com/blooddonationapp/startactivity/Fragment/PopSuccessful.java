package com.blooddonationapp.startactivity.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;

import androidx.annotation.Nullable;

import com.blooddonationapp.startactivity.R;

public class PopSuccessful extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.popsuccessfulrequest);

        DisplayMetrics dn = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dn);

        int width = dn.widthPixels;
        int height = dn.heightPixels;

        getWindow().setLayout((int) (width * .8), (int) (height * .6));


    }
}