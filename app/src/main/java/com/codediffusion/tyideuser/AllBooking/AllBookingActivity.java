package com.codediffusion.tyideuser.AllBooking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.codediffusion.tyideuser.R;
import com.codediffusion.tyideuser.adapter.SectionsPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class AllBookingActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private SectionsPagerAdapter sectionsPagerAdapter;
    private TabLayout tabLayout;
    private ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_booking);
        Initialization();
    }

    private void Initialization() {
        viewPager=findViewById(R.id.viewpager);
        sectionsPagerAdapter=new SectionsPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(sectionsPagerAdapter);

        tabLayout=findViewById(R.id.tab_layout);
        iv_back=findViewById(R.id.iv_back);
        tabLayout.setupWithViewPager(viewPager);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


}