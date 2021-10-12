package com.codediffusion.tyideuser.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.codediffusion.tyideuser.Fragment.FragmentAllBooking;
import com.codediffusion.tyideuser.Fragment.FragmentCancelledBooking;
import com.codediffusion.tyideuser.Fragment.FragmentCompleteBooking;
import com.codediffusion.tyideuser.Fragment.FragmentOnGoingBooking;


public class SectionsPagerAdapter extends FragmentPagerAdapter {
    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                FragmentAllBooking allBooking = new FragmentAllBooking();
                return allBooking;

            case 1:
                FragmentOnGoingBooking fragmentOnGoingBooking = new FragmentOnGoingBooking();
                return fragmentOnGoingBooking;

            case 2:
                FragmentCompleteBooking fragmentCompleteBooking = new FragmentCompleteBooking();
                return fragmentCompleteBooking;

            case 3:
                FragmentCancelledBooking fragmentCancelledBooking = new FragmentCancelledBooking();
                return fragmentCancelledBooking;

            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 4;
    }

    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "All Booking";
            case 1:
                return "Ongoing";
            case 2:
                return "Complete";
            case 3:
                return "Cancelled";
            default:
                return null;
        }
    }
}
