package com.blooddonationapp.startactivity.Utils;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.blooddonationapp.startactivity.Fragment.RequestCompletedFragment;
import com.blooddonationapp.startactivity.Fragment.RequestFragment;
import com.blooddonationapp.startactivity.Fragment.RequestPendingFragment;

public class MyViewPagerAdapter extends FragmentStateAdapter {
    public MyViewPagerAdapter(@NonNull RequestFragment fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
       switch (position){
           case 0:
               return new RequestPendingFragment();
           case 1:
               return new RequestCompletedFragment();
           default:
               return new RequestPendingFragment();
       }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
