package com.moadab.colornotesapp.Common;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.google.android.material.tabs.TabLayout;
import com.moadab.colornotesapp.Adapter.SliderAdapter;
import com.moadab.colornotesapp.R;
import com.moadab.colornotesapp.SingUpActivity;

public class OnBoardingActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private Button btnNext,btnStart;
    private TabLayout tabLayout;

    private int sliderImage[]={R.drawable.slider_one , R.drawable.slider_two , R.drawable.slider_three};
    private int sliderTitle[]={R.string.slider_title_one , R.string.slider_title_two , R.string.slider_title_three};
    private int sliderDescription[]={R.string.slider_desc_one , R.string.slider_desc_two , R.string.slider_desc_three};
    private SliderAdapter sliderAdapter;

    private int position = 0;
    Animation appNameAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);

        viewPager = findViewById(R.id.slider);
        tabLayout = findViewById(R.id.tab_mode);
        btnNext = findViewById(R.id.btn_next);
        btnStart = findViewById(R.id.btn_start);

        sliderAdapter = new SliderAdapter(OnBoardingActivity.this,sliderImage,sliderTitle,sliderDescription);
        viewPager.setAdapter(sliderAdapter);

        tabLayout.setupWithViewPager(viewPager);


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                position = viewPager.getCurrentItem();

                if(sliderImage.length > position ){
                    viewPager.setCurrentItem(++position);
                }
                if(sliderImage.length - 1 == position ){
                    animateViewIn();
                }
            }
        });

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if (position == 2){
                    animateViewIn();
                }
                else {
                    animateViewOut();
                }

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OnBoardingActivity.this, SingUpActivity.class));
                finish();
            }
        });

    }

    private void animateViewOut() {
        tabLayout.setVisibility(View.VISIBLE);
        btnNext.setVisibility(View.VISIBLE);
        btnStart.setVisibility(View.INVISIBLE);
    }


    private void animateViewIn() {

        appNameAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_on_boarding);
        btnStart.setAnimation(appNameAnimation);
        tabLayout.setVisibility(View.INVISIBLE);
        btnNext.setVisibility(View.INVISIBLE);
        btnStart.setVisibility(View.VISIBLE);

    }

}