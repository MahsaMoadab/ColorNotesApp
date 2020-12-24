package com.moadab.colornotesapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.moadab.colornotesapp.Common.OnBoardingActivity;
import com.moadab.colornotesapp.R;

public class SliderAdapter extends PagerAdapter {

    OnBoardingActivity onBoardingActivity;
    int[] sliderImage;
    int[] sliderTitle;
    int[] sliderDescription;

    public SliderAdapter(OnBoardingActivity onBoardingActivity, int[] sliderImage, int[] sliderTitle, int[] sliderDescription) {
        this.onBoardingActivity = onBoardingActivity;
        this.sliderImage = sliderImage;
        this.sliderTitle = sliderTitle;
        this.sliderDescription = sliderDescription;
    }


    @Override
    public int getCount() {
        return sliderImage.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        View view = LayoutInflater.from(onBoardingActivity).inflate(R.layout.sliders_item,container,false);

        ImageView imgBackgroud;
        TextView txtTitle;
        TextView txtDesc;

        imgBackgroud = view.findViewById(R.id.slider_img);
        txtTitle = view.findViewById(R.id.slider_title);
        txtDesc = view.findViewById(R.id.slider_desc);

        imgBackgroud.setBackgroundResource(sliderImage[position]);
        txtTitle.setText(sliderTitle[position]);
        txtDesc.setText(sliderDescription[position]);


        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((View) object);

    }
}
