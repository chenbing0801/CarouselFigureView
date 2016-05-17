package com.my.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.DrawableRes;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.my.R;
import com.my.view.switchanimotion.ViewPagerScroller;

/**
 * AUTHOR:       Yuan.Meng
 * E-MAIL:       mengyuanzz@126.com
 * CREATE-TIME:  16/5/16/上午10:49
 * DESC:
 */
public class CarouselFigureView extends RelativeLayout {

    //----------－－－－-控件－－－－－－－－－－－
    private ViewPager viewPager;//ImageView容器
    private LinearLayout indicationPointLayout; //指示点容器

    //-------------－－自定义属性－－－－－－－－－－－－
    private boolean isAutoPlay = true; //是否自动切换
    private boolean isInfiniteLoop = true; //是否无限循环
    private boolean isNeedIndicationPoint = true;//是否需要指示点
    private float pointLeft_Right_Margin = 5;//指示点的间距，单位是dp
    private float pointBottomMargin = 2;//指示点上移的距离,单位是dp
    private int playIntervalTime = 3000;//自动切换间隔，默认3秒，单位毫秒
    private int pointBackgroundId = R.drawable.point_bg;//指示点背景，可自定义

    //－－－－－－－－－数据－－－－－－－－－－－
    private int itemCount; //条目数量
    private int lastPosition;//上一个显示的条目下标
    private List<String> urlList;//url集合
    private List<Integer> resourceList;//资源集合
    private List<ImageView> pictureList;//存放图片的ImageView
    //--------------其他－－－－－－－－－－－
    private boolean userIsTouchScreen = false; //用户是否触屏，当用户触屏时，应停止自动播放
    private int MAX_VALUE = Integer.MAX_VALUE; //最大值，用于无限循环
    private int SELCET_LOAD_MODE; //图片加载模式

    /**
     * 图片的加载方式
     */
    public interface LOAD_MODE {
        int URL = 1001, RESOURCE = 1002;
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (isAutoPlay) {
                if (!userIsTouchScreen) {
                    viewPager.setCurrentItem(lastPosition + 1);
                }
                handler.sendEmptyMessageDelayed(88, playIntervalTime);
            }
        }
    };


    public CarouselFigureView(Context context) {
        this(context, null);
    }

    public CarouselFigureView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CarouselFigureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        parseCustomAttributes(context, attrs);

        initView(context, attrs, defStyleAttr);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {


        initViewPager(context);

        if (isNeedIndicationPoint) {
            initPonitLinearLayout(context);
        }

    }

    /**
     * 解析自定义属性
     */
    private void parseCustomAttributes(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CarouselFigureView);
        // 获得xml里定义的属性,格式为 名称_属性名 后面是默认值
        isAutoPlay = a.getBoolean(R.styleable.CarouselFigureView_isAutoPlay, true);
        isInfiniteLoop = a.getBoolean(R.styleable.CarouselFigureView_isInfiniteLoop, true);
        isNeedIndicationPoint = a.getBoolean(R.styleable.CarouselFigureView_isNeedIndicationPoint, true);
        pointLeft_Right_Margin = a.getDimension(R.styleable.CarouselFigureView_pointLeft_Right_Margin, 5);
        pointBottomMargin = a.getDimension(R.styleable.CarouselFigureView_pointBottomMargin, 2);
        playIntervalTime = a.getInt(R.styleable.CarouselFigureView_playIntervalTime, 3000);
        pointBackgroundId = a.getResourceId(R.styleable.CarouselFigureView_pointBackground, R.drawable.point_bg);
        // 为了保持以后使用该属性一致性,返回一个绑定资源结束的信号给资源
        a.recycle();
    }

    /**
     * 初始化指示点布局
     *
     * @param context
     */
    private void initPonitLinearLayout(Context context) {
        indicationPointLayout = new LinearLayout(context);
        indicationPointLayout.setOrientation(LinearLayout.HORIZONTAL);
        indicationPointLayout.setGravity(Gravity.CENTER_HORIZONTAL);


        RelativeLayout.LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(ALIGN_PARENT_BOTTOM);
        this.addView(indicationPointLayout, layoutParams);


    }


    private void initViewPager(Context context) {
        viewPager = new ViewPager(context);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (isNeedIndicationPoint) {
                    indicationPointLayout.getChildAt(lastPosition % itemCount).setSelected(false);
                    indicationPointLayout.getChildAt(position % itemCount).setSelected(true);
                }
                lastPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case ViewPager.SCROLL_STATE_IDLE://空闲状态
                        userIsTouchScreen = false;
                        break;
                    case ViewPager.SCROLL_STATE_DRAGGING://用户滑动状态
                        userIsTouchScreen = true;
                        break;
                }

            }
        });
        RelativeLayout.LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        this.addView(viewPager, layoutParams);
    }


    /**
     * set data
     *
     * @param urlList：图片的url地址
     */
    public void setURL(List<String> urlList) {
        this.urlList = urlList;
        SELCET_LOAD_MODE = LOAD_MODE.URL;
        itemCount = urlList.size();
    }

    /**
     * set data
     *
     * @param resourceList 图片资源地址
     */
    public void setResourceList(@DrawableRes List<Integer> resourceList) {
        this.resourceList = resourceList;
        SELCET_LOAD_MODE = LOAD_MODE.RESOURCE;
        itemCount = resourceList.size();
    }

    /**
     * 改变ViewPager的切换方式
     * @param transformer   可自定义ViewPager.PageTransformer，此类代表ViewPager切换动画
     */
    public void setViewPagerSwitchStyle(ViewPager.PageTransformer transformer) {
        viewPager.setPageTransformer(true, transformer);

    }

    /**
     * 改变ViewPager的切换速度
     * @param switchSpeed 单位毫秒
     */
    public void setViewPagerSwitchSpeed(int switchSpeed) {
        if(switchSpeed >= playIntervalTime) throw new RuntimeException("CarouselFigureView:you set speed is so Slow, speedTime should > playIntervalTime::你设置的切换速度必须小于轮播图的切换时间");

        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            ViewPagerScroller scroller = new ViewPagerScroller(this.getContext(),
                    new AccelerateInterpolator());
            field.set(viewPager, scroller);
            scroller.setmDuration(switchSpeed);
        } catch (Exception e) {
        }
    }


    /**
     * 在设置完数据之后调用此方法进行显示
     */
    public void startLoad()  {

        if (SELCET_LOAD_MODE == 0)  throw new RuntimeException("CarouselFigureView:you must set data use setURL() or setResourceList()::你必须设置数据 可以使用setUrl()或者setResourceList()来设置数据");
        if (isNeedIndicationPoint) {
            addIndicationPoint();
        }
        loadPictureToImageView();
        viewPager.setAdapter(new MyViewPagerAdapter());
        if (isInfiniteLoop) {
            viewPager.setCurrentItem(MAX_VALUE / 2 - (MAX_VALUE / 2) % itemCount);
        }
        if (isAutoPlay) {
            handler.sendEmptyMessageDelayed(88, playIntervalTime);
        }
    }

    /**
     * 根据数据将图片加载至ImageView，并将ImageView保存至List集合中
     */
    private void loadPictureToImageView() {
        pictureList = new ArrayList<>();
        ImageView imageView;
        for (int i = 0; i < itemCount; i++) {
            imageView = new ImageView(this.getContext());
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            switch (SELCET_LOAD_MODE) {
                case LOAD_MODE.URL:
                    loadImgByUrl(urlList.get(i), imageView);
                    break;
                case LOAD_MODE.RESOURCE:
                    loadImgByResourceId(resourceList.get(i), imageView);
                    break;
            }
            pictureList.add(imageView);
        }

    }


    /**
     * 添加指示点
     */
    private void addIndicationPoint() {
        ImageView pointImageView;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins((int) pointLeft_Right_Margin, 0, (int) pointLeft_Right_Margin, (int) pointBottomMargin);

        for (int i = 0; i < itemCount; i++) {
            pointImageView = new ImageView(this.getContext());
            pointImageView.setBackgroundResource(pointBackgroundId);
            if (i == 0) {
                pointImageView.setSelected(true);
            } else {
                pointImageView.setSelected(false);
            }

            indicationPointLayout.addView(pointImageView, layoutParams);
        }
    }


    private class MyViewPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            if (isInfiniteLoop) {
                return MAX_VALUE;
            } else {
                return itemCount;
            }
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(pictureList.get(position % itemCount));
            return pictureList.get(position % itemCount);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(pictureList.get(position % itemCount));
        }


    }

    //------------------other----------------------------------


    private void loadImgByUrl(String url, final ImageView imageView) {
        Glide.with(this.getContext())
                .load(url)
                .placeholder(R.mipmap.img_empty)
                .crossFade()
                .into(imageView);
    }

    private void loadImgByResourceId(@DrawableRes int resource, ImageView imageView) {
        Glide.with(this.getContext())
                .load(resource)
                .placeholder(R.mipmap.img_empty)
                .crossFade()
                .into(imageView);
    }



}
