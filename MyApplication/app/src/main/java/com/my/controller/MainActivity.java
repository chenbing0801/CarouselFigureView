package com.my.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

import com.my.R;
import com.my.view.CarouselFigureView;
import com.my.view.switchanimotion.DepthPageTransformer;

public class MainActivity extends AppCompatActivity {

    private CarouselFigureView carouselFigureView;

    /*
    http://o.ypgimg.com/content/2016/5/16/3b61698c-cf8b-4abe-9c39-d474b454d790.jpg
    http://o.ypgimg.com/content/2016/5/16/88d50d38-09fb-4d3e-bd0c-fc52e2a26c74.jpg
    http://o.ypgimg.com/content/2016/5/16/e5a7b0e3-1a21-4e66-adc8-17a1b7c1a92f.jpg
    http://o.ypgimg.com/content/2016/5/16/8bae5357-d542-4e52-ad5f-ad7d021fc163.jpg
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        carouselFigureView = (CarouselFigureView) findViewById(R.id.carousel_figure_view);
        ArrayList<String> url = new ArrayList<>();

        url.add("http://o.ypgimg.com/content/2016/5/16/3b61698c-cf8b-4abe-9c39-d474b454d790.jpg");
        url.add("http://o.ypgimg.com/content/2016/5/16/88d50d38-09fb-4d3e-bd0c-fc52e2a26c74.jpg");
        url.add("http://o.ypgimg.com/content/2016/5/16/e5a7b0e3-1a21-4e66-adc8-17a1b7c1a92f.jpg");
        url.add("http://o.ypgimg.com/content/2016/5/16/8bae5357-d542-4e52-ad5f-ad7d021fc163.jpg");
        carouselFigureView.setURL(url);
        carouselFigureView.startLoad();
        carouselFigureView.setViewPagerSwitchStyle(new DepthPageTransformer());
        carouselFigureView.setViewPagerSwitchSpeed(200);

    }


}
